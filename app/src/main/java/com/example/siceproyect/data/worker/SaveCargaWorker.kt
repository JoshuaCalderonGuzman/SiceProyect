package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.siceproject.data.local.entities.CargaEntity
import com.example.siceproyect.SICENETApplication

class SaveCargaWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // Recibe los datos del Worker 1
        val jsonCarga = inputData.getString("CARGA_JSON") ?: return Result.failure()
        val control = inputData.getString("CONTROL") ?: return Result.failure()

        val app = applicationContext as SICENETApplication
        val localRepository = app.container.localRepository

        return try {
            val entity = CargaEntity(
                control = control,
                jsonData = jsonCarga,
                ultimaActualizacion = System.currentTimeMillis()
            )

            // Guarda en la base de datos local
            localRepository.insertCarga(entity)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}