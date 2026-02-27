package com.example.siceproject.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carga")
data class CargaEntity(
    @PrimaryKey
    val control: String,
    val jsonData: String,
    val ultimaActualizacion: Long
)