package com.example.siceproyect.data

import android.util.Log
import com.example.siceproyect.network.SICENETWService
import com.example.siceproyect.network.bodyacceso
import com.example.siceproyect.network.datos
import com.example.siceproyect.network.califUnidades
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

interface SNRepository {
    suspend fun acceso(m: String, p: String): LoginResult
    suspend fun alumnoDatos(): Alumno

}

class NetworSNRepository(
    private val snApiService: SICENETWService
) : SNRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun acceso(m: String, p: String): LoginResult {

        val response = snApiService.acceso(
            bodyacceso.format(m, p)
                .toRequestBody("text/xml; charset=utf-8".toMediaType())
        )

        val xml = response.string()
        logXML(xml, "Login")

        if (!xml.contains("<accesoLoginResult>")) {
            return LoginResult(false, "Respuesta inválida del servidor")
        }

        val result = xml.substringAfter("<accesoLoginResult>")
            .substringBefore("</accesoLoginResult>")

        return if (result.contains("RECHAZADO", true) || result.isEmpty()) {
            LoginResult(false, "Usuario o contraseña incorrectos")
        } else {
            LoginResult(true, "Login correcto")
        }
    }


    override suspend fun alumnoDatos(): Alumno {
        //Devolver objetos
        val response = snApiService.alumnoDatos(datos.toRequestBody("text/xml; charset=utf-8".toMediaType()))
        val xml = response.string()
        logXML(xml, "AlumnoDatos")
        return parseAlumno(xml)

    }




    fun logXML(xml: String, tag: String = "XML_DEBUG") {
        val maxLogSize = 1000
        for (i in xml.indices step maxLogSize) {
            Log.d(tag, xml.substring(i, minOf(i + maxLogSize, xml.length)))
        }
    }



}