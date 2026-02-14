package com.example.siceproyect.data

import android.util.Log
import org.json.JSONObject

data class Alumno(
    val fechaReins: String = "",
    val modEducativo: Int = 0,
    val adeudo: Boolean = false,
    val urlFoto: String = "",
    val adeudoDescriptivo: String = "",
    val inscrito: Boolean = false,
    val estatus: String = "",
    val semActual: String = "",
    val cdtosAcumulados: Int = 0,
    val cdtosActuales: Int = 0,
    val especialidad: String = "",
    val carrera: String="",
    val liniamiento: Int = 0,
    val nombre: String = "",
    val matricula: String = "",

)

fun parseAlumno(xml: String): Alumno {



    val jsonString = xml
        .substringAfter("<getAlumnoAcademicoWithLineamientoResult>")
        .substringBefore("</getAlumnoAcademicoWithLineamientoResult>")
        .trim()

    val json = JSONObject(jsonString)

    return Alumno(
        fechaReins = json.optString("fechaReins"),
        modEducativo = json.optInt("modEducativo"),
        adeudo = json.optBoolean("adeudo"),
        urlFoto = json.optString("urlFoto"),
        adeudoDescriptivo = json.optString("adeudoDescriptivo"),
        inscrito = json.optBoolean("inscrito"),
        estatus = json.optString("estatus"),
        semActual = json.optString("semActual"),
        cdtosAcumulados = json.optInt("cdtosAcumulados"),
        cdtosActuales = json.optInt("cdtosActuales"),
        especialidad = json.optString("especialidad"),
        carrera = json.optString("carrera"),
        liniamiento = json.optInt("liniamiento"),
        nombre = json.optString("nombre"),
        matricula = json.optString("matricula")

    )
}