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
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun acceso(m: String, p: String): String {

        //callHTTPS()
        val res = snApiService.acceso(bodyacceso.format(m,p).toRequestBody() )

        Log.d("RXML", res.string() )
        /* Log.d("RXML", res.body?.accesoLoginResponse?.accesoLoginResult.toString() )

         return res.body?.accesoLoginResponse?.accesoLoginResult.toString()*/
        /*Log.d("RXML", res.message() )
        return res.message()*/
        return ""
    }

    override suspend fun alumno_Datos(): String {
        val ret = snApiService.alumno_Datos(datos.toRequestBody() )
        Log.d("RXML", ret.string() )
        return ""
    }



}