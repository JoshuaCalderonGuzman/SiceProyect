package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.*
import com.example.siceproyect.data.local.AppDatabaseProvider
import com.example.siceproyect.data.local.entity.AlumnoEntity
import com.example.siceproyect.data.repository.LocalRepository

class SaveLoginWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {

        val db = AppDatabaseProvider.get(applicationContext)
        val repo = LocalRepository(db)

        val alumno = AlumnoEntity(
            control = inputData.getString("control")!!,
            nombre = inputData.getString("nombre")!!,
            carrera = inputData.getString("carrera")!!,
            ultimaActualizacion = System.currentTimeMillis()
        )

        repo.saveAlumno(alumno)

        return Result.success()
    }
}
