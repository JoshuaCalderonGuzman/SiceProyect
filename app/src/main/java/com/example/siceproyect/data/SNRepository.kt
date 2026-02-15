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
    suspend fun kardex(lineamiento: Int): KardexCompleto
    suspend fun califFinal(lineamiento: Int): List<CalificacionFinal>
    suspend fun califUnidades():  List<MateriaUnidades>
    suspend fun cargaAcademica(): List<MateriaCarga>

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
    override suspend fun kardex(lineamiento: Int): KardexCompleto {
        return try {

            val body = cardex.format(lineamiento.toString()).toRequestBody("text/xml".toMediaTypeOrNull())
            val response = snApiService.cardex(body)
            val xml = response.string()

            logXML(xml, "KARDEX_SUCCESS")
            parseKardex(xml)
        } catch (e: Exception) {
            Log.e("KARDEX_ERROR", {e.message}.toString())
            KardexCompleto(emptyList(), KardexResumen(0.0, 0, 0, 0.0))
        }
    }
    override suspend fun califFinal(lineamiento: Int):  List<CalificacionFinal> {
        return try {

            val body = califFinal.format(lineamiento.toString()).toRequestBody("text/xml".toMediaTypeOrNull())
            val response = snApiService.califFinal(body)
            val xml = response.string()

            logXML(xml, "CALIFFINAL_SUCCESS")
            parseCalifFinal(xml)
        } catch (e: Exception) {
            Log.e("CALIFFINAL_ERROR", {e.message}.toString())
            emptyList()
        }
    }
    override suspend fun califUnidades():  List<MateriaUnidades> {
        return try {

            val body = califUnidades.toRequestBody("text/xml".toMediaTypeOrNull())
            val response = snApiService.califUnidades(body)
            val xml = response.string()

            logXML(xml, "CALIFUNIDADES_SUCCESS")
            parseUnidades(xml)
        } catch (e: Exception) {
            Log.e("CALIFUNIDADES_ERROR", {e.message}.toString())
            emptyList()
        }
    }

    override suspend fun cargaAcademica(): List<MateriaCarga> {
        return try {

            val body = cargaAcademica.toRequestBody("text/xml".toMediaTypeOrNull())
            val response = snApiService.cargaAcademica(body)
            val xml = response.string()

            logXML(xml, "CARGAACADEMICA_SUCCESS")
            parseCargaAcademica(xml)
        } catch (e: Exception) {
            Log.e("CARGAACADEMICA_ERROR", {e.message}.toString())
            emptyList()
        }
    }




    fun logXML(xml: String, tag: String = "XML_DEBUG") {
        val maxLogSize = 1000
        for (i in xml.indices step maxLogSize) {
            Log.d(tag, xml.substring(i, minOf(i + maxLogSize, xml.length)))
        }
    }



}