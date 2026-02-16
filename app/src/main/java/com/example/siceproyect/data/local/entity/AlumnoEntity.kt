package com.example.siceproyect.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alumno")
data class AlumnoEntity(
    @PrimaryKey val control: String,
    val nombre: String,
    val carrera: String,
    val ultimaActualizacion: Long
)
