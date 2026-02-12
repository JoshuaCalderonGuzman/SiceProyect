package com.example.siceproyect.data

import com.example.siceproyect.network.SICENETWService
import com.example.siceproyect.network.bodyacceso
import com.example.siceproyect.network.datos
import okhttp3.RequestBody.Companion.toRequestBody

interface SNRepository {
    suspend fun acceso(m: String, p: String): LoginResult
    suspend fun alumno_Datos(): Alumno
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
        val result = xml.substringAfter("<accesoLoginResult>")
            .substringBefore("</accesoLoginResult>")

        return if (result.contains("RECHAZADO", true) || result.isEmpty()) {
            LoginResult(false, "Usuario o contraseña incorrectos")
        } else {
            LoginResult(true, "Login correcto")
        }
    }


    override suspend fun alumno_Datos(): Alumno {
        //Devolver objetos
        val response = snApiService.alumno_Datos(datos.toRequestBody())
        val xml = response.string()
        return parseAlumno(xml)

    }



}