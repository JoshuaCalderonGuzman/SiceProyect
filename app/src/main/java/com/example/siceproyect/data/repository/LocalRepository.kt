package com.example.siceproyect.data.repository

import com.example.siceproyect.data.local.AppDatabase
import com.example.siceproyect.data.local.entity.AlumnoEntity

class LocalRepository(
    private val db: AppDatabase
) {

    suspend fun saveAlumno(entity: AlumnoEntity) =
        db.alumnoDao().insert(entity)

    suspend fun getAlumno(control: String) =
        db.alumnoDao().get(control)
}
