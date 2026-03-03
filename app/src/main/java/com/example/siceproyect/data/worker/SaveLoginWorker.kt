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

        //Obtenemos los datos pasados al Worker
        val controlStr = inputData.getString("control") ?: return Result.failure()
        val jsonDataStr = inputData.getString("jsonData") ?: return Result.failure()

        //Creamos la entidad con el nuevo formato (usando jsonData)
        val alumno = AlumnoEntity(
            control = controlStr,
            jsonData = jsonDataStr,
            ultimaActualizacion = System.currentTimeMillis()
        )

        repo.saveAlumno(alumno)

        return Result.success()
    }
}