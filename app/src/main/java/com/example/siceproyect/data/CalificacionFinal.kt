package com.example.siceproyect.data

import org.json.JSONArray

data class CalificacionFinal(
    val calif: Int,
    val acred: String,
    val grupo: String,
    val materia: String,
    val observaciones: String
)
fun parseCalifFinal(xml: String): List<CalificacionFinal> {
    val jsonString = xml
        .substringAfter("<getAllCalifFinalByAlumnosResult>")
        .substringBefore("</getAllCalifFinalByAlumnosResult>")
        .trim()

    val jsonArray = JSONArray(jsonString)
    val listaCalificaciones = mutableListOf<CalificacionFinal>()

    for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)
        listaCalificaciones.add(
            CalificacionFinal(
                calif = obj.optInt("calif"),
                acred = obj.optString("acred"),
                grupo = obj.optString("grupo"),
                materia = obj.optString("materia"),
                observaciones = obj.optString("Observaciones")
            )
        )
    }
    return listaCalificaciones
}