package com.example.siceproyect.data

import org.json.JSONArray

data class MateriaUnidades(
    val nombre: String,
    val grupo: String,
    val observaciones: String,
    val unidadesActivas: String,
    val calificaciones: List<Int?>
)
fun parseUnidades(xml: String): List<MateriaUnidades> {
    val jsonString = xml
        .substringAfter("<getCalifUnidadesByAlumnoResult>")
        .substringBefore("</getCalifUnidadesByAlumnoResult>")
        .trim()

    val jsonArray = JSONArray(jsonString)
    val lista = mutableListOf<MateriaUnidades>()

    for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)

        // Creamos la lista de calificaciones dinámicamente
        val califs = (1..13).map { index ->
            val key = "C$index"
            if (obj.isNull(key)) null else obj.optString(key).toIntOrNull()
        }

        lista.add(
            MateriaUnidades(
                nombre = obj.optString("Materia"),
                grupo = obj.optString("Grupo"),
                observaciones = obj.optString("Observaciones"),
                unidadesActivas = obj.optString("UnidadesActivas"),
                calificaciones = califs
            )
        )
    }
    return lista
}