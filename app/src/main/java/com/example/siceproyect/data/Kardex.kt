package com.example.siceproyect.data

import org.json.JSONObject

data class KardexMateria(
    val clvMat: String,
    val materia: String,
    val cdts: Int,
    val calif: Int,
    val acred: String,
    val periodo: String,
    val anio: String,
    val semestre: String
)

data class KardexResumen(
    val promedioGral: Double,
    val cdtsAcumulados: Int,
    val cdtsPlan: Int,
    val avance: Double
)

data class KardexCompleto(
    val materias: List<KardexMateria>,
    val resumen: KardexResumen
)

fun parseKardex(xml: String): KardexCompleto {
    val jsonString = xml
        .substringAfter("<getAllKardexConPromedioByAlumnoResult>")
        .substringBefore("</getAllKardexConPromedioByAlumnoResult>")
        .trim()

    val jsonRoot = JSONObject(jsonString)

    // Parsear la lista de materias
    val jsonArray = jsonRoot.getJSONArray("lstKardex")
    val materias = mutableListOf<KardexMateria>()

    for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)
        materias.add(KardexMateria(
            clvMat = obj.optString("ClvOfiMat"),
            materia = obj.optString("Materia"),
            cdts = obj.optInt("Cdts"),
            calif = obj.optInt("Calif"),
            acred = obj.optString("Acred"),
            periodo = obj.optString("P1"),
            anio = obj.optString("A1"),
            semestre = obj.optString("S1")
        ))
    }

    // Parsear el promedio/resumen
    val promObj = jsonRoot.getJSONObject("Promedio")
    val resumen = KardexResumen(
        promedioGral = promObj.optDouble("PromedioGral"),
        cdtsAcumulados = promObj.optInt("CdtsAcum"),
        cdtsPlan = promObj.optInt("CdtsPlan"),
        avance = promObj.optDouble("AvanceCdts")
    )

    return KardexCompleto(materias, resumen)
}