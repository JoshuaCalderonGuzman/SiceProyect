package com.example.siceproyect.data

import org.json.JSONObject

data class Alumno(
    val nombre: String,
    val carrera: String,
    val matricula: String,
    val inscrito: Boolean,
    val estatus: String,
    val semActual: String,
    val cdtosAcumulados: Int,
    val especialidad: String
)

fun parseAlumno(xml: String): Alumno {

    val jsonString = xml
        .substringAfter("<getAlumnoAcademicoWithLineamientoResult>")
        .substringBefore("</getAlumnoAcademicoWithLineamientoResult>")
        .trim()

    val json = JSONObject(jsonString)

    return Alumno(
        nombre = json.getString("nombre"),
        carrera = json.getString("carrera"),
        matricula = json.getString("matricula"),
        inscrito = json.getBoolean("inscrito"),
        estatus = json.getString("estatus"),
        semActual = json.getString("semActual"),
        cdtosAcumulados = json.getInt("cdtosAcumulados"),
        especialidad = json.getString("especialidad")

    )
}