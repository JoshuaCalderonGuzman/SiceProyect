package com.example.siceproyect.data

import com.example.siceproyect.network.SICENETWService
import com.example.siceproyect.network.bodyacceso
import com.example.siceproyect.network.datos
import okhttp3.RequestBody.Companion.toRequestBody

interface SNRepository {
    suspend fun acceso(m: String, p: String): String
    suspend fun alumno_Datos(): String
}

class NetworSNRepository(
    private val snApiService: SICENETWService
) : SNRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun acceso(m: String, p: String): String {
        //Devolver objetos
        snApiService.con()
        val response = snApiService.acceso(bodyacceso.format(m, p).toRequestBody())
        val xml = response.string()

        return xml.substringAfter("<accesoLoginResult>")
            .substringBefore("</accesoLoginResult>")
    }

    override suspend fun alumno_Datos(): String {
        //Devolver objetos
        val ret = snApiService.alumno_Datos(datos.toRequestBody())
        return ret.string()
    }



}