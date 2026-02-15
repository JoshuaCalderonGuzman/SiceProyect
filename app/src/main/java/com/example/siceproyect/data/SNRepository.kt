package com.example.siceproyect.data

import android.util.Log
import com.example.siceproyect.network.SICENETWService
import com.example.siceproyect.network.bodyacceso
import com.example.siceproyect.network.califFinal
import com.example.siceproyect.network.datos
import com.example.siceproyect.network.califUnidades
import com.example.siceproyect.network.cardex
import com.example.siceproyect.network.cargaAcademica
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

interface SNRepository {
    suspend fun acceso(m: String, p: String): LoginResult
    suspend fun alumnoDatos(): Alumno
    suspend fun kardex(): String
    suspend fun califFianl(): String
    suspend fun califUnidades(): String
    suspend fun cargaAcademica(): String

}

class NetworSNRepository(
    private val snApiService: SICENETWService
) : SNRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun acceso(m: String, p: String): LoginResult {
        //Devolver objetos
        snApiService.con()
        val response = snApiService.acceso(bodyacceso.format(m, p).toRequestBody())
        val xml = response.string()
        logXML(xml, "Login")
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
        val response = snApiService.alumnoDatos(datos.toRequestBody())
        val xml = response.string()
        logXML(xml, "AlumnoDatos")
        return parseAlumno(xml)

    }
    override suspend fun kardex(): String {
        return try {

            val body = cardex.format("2").toRequestBody("text/xml".toMediaTypeOrNull())
            val response = snApiService.cardex(body)
            val xml = response.string()

            logXML(xml, "KARDEX_SUCCESS")
            xml
        } catch (e: Exception) {
            Log.e("KARDEX_ERROR", {e.message}.toString())
            "Error"
        }
    }
    override suspend fun califFianl(): String {
        return try {

            val body = califFinal.format("2").toRequestBody("text/xml".toMediaTypeOrNull())
            val response = snApiService.califFinal(body)
            val xml = response.string()

            logXML(xml, "CALIFFINAL_SUCCESS")
            xml
        } catch (e: Exception) {
            Log.e("CALIFFINAL_ERROR", {e.message}.toString())
            "Error"
        }
    }
    override suspend fun califUnidades(): String {
        return try {

            val response = snApiService.califUnidades(califUnidades.toRequestBody())
            val xml = response.string()

            logXML(xml, "CALIFUNIDADES_SUCCESS")
            xml
        } catch (e: Exception) {
            Log.e("CALIFUNIDADES_ERROR", {e.message}.toString())
            "Error"
        }
    }

    override suspend fun cargaAcademica(): String {
        return try {

            val response = snApiService.cargaAcademica(cargaAcademica.toRequestBody())
            val xml = response.string()

            logXML(xml, "CARGAACADEMICA_SUCCESS")
            xml
        } catch (e: Exception) {
            Log.e("CARGAACADEMICA_ERROR", {e.message}.toString())
            "Error"
        }
    }




    fun logXML(xml: String, tag: String = "XML_DEBUG") {
        val maxLogSize = 1000
        for (i in xml.indices step maxLogSize) {
            Log.d(tag, xml.substring(i, minOf(i + maxLogSize, xml.length)))
        }
    }



}