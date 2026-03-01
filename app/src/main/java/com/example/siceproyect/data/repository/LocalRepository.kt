package com.example.siceproyect.data.repository

import com.example.siceproyect.data.local.AppDatabase
import com.example.siceproyect.data.local.entity.AlumnoEntity
import com.example.siceproject.data.local.entities.CargaEntity
import com.example.siceproject.data.local.entities.KardexEntity
import com.example.siceproject.data.local.entities.CalifFinalEntity
import com.example.siceproject.data.local.entities.CalifUnidadesEntity

class LocalRepository(
    private val db: AppDatabase
) {
    // --- ALUMNO ---
    suspend fun saveAlumno(entity: AlumnoEntity) = db.alumnoDao().insert(entity)
    suspend fun getAlumno(control: String) = db.alumnoDao().get(control)

    // --- CARGA ACADÉMICA ---
    suspend fun insertCarga(carga: CargaEntity) = db.cargaDao().insert(carga)
    suspend fun getCargaByControl(control: String) = db.cargaDao().getByControl(control)

    // --- KARDEX ---
    suspend fun insertKardex(kardex: KardexEntity) = db.kardexDao().insert(kardex)
    suspend fun getKardexByControl(control: String) = db.kardexDao().getByControl(control)

    // --- CALIFICACIONES FINALES ---
    suspend fun insertCalifFinal(calif: CalifFinalEntity) = db.califFinalDao().insert(calif)
    suspend fun getCalifFinalByControl(control: String) = db.califFinalDao().getByControl(control)

    // --- CALIFICACIONES UNIDADES ---
    suspend fun insertCalifUnidades(calif: CalifUnidadesEntity) = db.califUnidadesDao().insert(calif)
    suspend fun getCalifUnidadesByControl(control: String) = db.califUnidadesDao().getByControl(control)
}