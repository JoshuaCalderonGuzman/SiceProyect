package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.siceproject.data.local.entities.CalifUnidadesEntity
import com.example.siceproyect.SICENETApplication

class SaveUnidadesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val isReady = inputData.getBoolean("UNIDADES_READY", false)
        if (!isReady) return Result.failure()

        val control = inputData.getString("CONTROL") ?: return Result.failure()

        val prefs = applicationContext.getSharedPreferences("WorkerCache", Context.MODE_PRIVATE)
        val jsonUnidades = prefs.getString("UNIDADES_TEMP", null) ?: return Result.failure()

        val app = applicationContext as SICENETApplication
        val localRepository = app.container.localRepository

        return try {
            val entity = CalifUnidadesEntity(
                control = control,
                jsonData = jsonUnidades,
                ultimaActualizacion = System.currentTimeMillis()
            )
            // ⚠️ OJO: Asegúrate de que este sea el nombre correcto en LocalRepository
            localRepository.insertCalifUnidades(entity)

            prefs.edit().remove("UNIDADES_TEMP").apply()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}