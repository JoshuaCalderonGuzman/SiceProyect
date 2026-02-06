package com.example.siceproyect.data

import android.util.Log
import com.example.siceproyect.network.SICENETWService
import com.example.siceproyect.network.bodyacceso
import com.example.siceproyect.network.datos
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

interface SNRepository {
    suspend fun acceso(m: String, p: String): String
    suspend fun alumno_Datos(): String
}

class NetworSNRepository(
    private val snApiService: SICENETWService
) : com.example.siceproyect.data.SNRepository {
    private var sessionCookie: String? = null
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun acceso(m: String, p: String): String {
        snApiService.con()
        val response = snApiService.acceso(bodyacceso.format(m, p).toRequestBody())
        val xml = response.string()

        return xml.substringAfter("<accesoLoginResult>")
            .substringBefore("</accesoLoginResult>")
    }

    override suspend fun alumno_Datos(): String {
        val ret = snApiService.alumno_Datos(datos.toRequestBody())
        return ret.string()
    }



}