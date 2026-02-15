package com.example.siceproyect.data

import org.json.JSONArray

data class MateriaCarga(
    val materia: String,
    val grupo: String,
    val docente: String,
    val clvOficial: String,
    val estadoMateria: String,
    val creditos: Int,
    val observaciones: String,
    val semipresencial: String,
    val lunes: String,
    val martes: String,
    val miercoles: String,
    val jueves: String,
    val viernes: String,
    val sabado: String
) {
    fun obtenerHorarioPorDia(dia: String): String {
        return when (dia) {
            "Lunes" -> lunes
            "Martes" -> martes
            "Miercoles" -> miercoles
            "Jueves" -> jueves
            "Viernes" -> viernes
            "Sabado" -> sabado
            else -> ""
        }
    }
}


fun parseCargaAcademica(xml: String): List<MateriaCarga> {

    val jsonString = xml
        .substringAfter("<getCargaAcademicaByAlumnoResult>")
        .substringBefore("</getCargaAcademicaByAlumnoResult>")
        .trim()

    val jsonArray = JSONArray(jsonString)
    val lista = mutableListOf<MateriaCarga>()

    for (i in 0 until jsonArray.length()) {

        val obj = jsonArray.getJSONObject(i)

        lista.add(
            MateriaCarga(
                materia = obj.optString("Materia"),
                grupo = obj.optString("Grupo"),
                docente = obj.optString("Docente"),
                clvOficial = obj.optString("clvOficial"),
                estadoMateria = obj.optString("EstadoMateria"),
                creditos = obj.optInt("CreditosMateria"),
                observaciones = obj.optString("Observaciones"),
                semipresencial = obj.optString("Semipresencial"),
                lunes = obj.optString("Lunes"),
                martes = obj.optString("Martes"),
                miercoles = obj.optString("Miercoles"),
                jueves = obj.optString("Jueves"),
                viernes = obj.optString("Viernes"),
                sabado = obj.optString("Sabado")
            )
        )
    }

    return lista
}

