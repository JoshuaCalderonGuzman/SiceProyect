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

class NetworkSNRepository(
    private val snApiService: SICENETWService
) : SNRepository {

    override suspend fun acceso(m: String, p: String): String {

        val requestBody = bodyacceso
            .format(m, p)
            .toRequestBody("text/xml; charset=utf-8".toMediaType())

        val response = snApiService.acceso(requestBody)

        val xml = response.string()
        Log.d("SOAP_LOGIN", xml)

        return xml
    }

    override suspend fun alumno_Datos(): String {

        val requestBody =
            datos.toRequestBody("text/xml; charset=utf-8".toMediaType())

        val response = snApiService.alumno_Datos(requestBody)

        val xml = response.string()
        Log.d("SOAP_DATOS", xml)

        return xml
    }
}