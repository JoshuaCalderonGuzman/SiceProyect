package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.siceproject.data.local.entities.CalifFinalEntity
import com.example.siceproyect.SICENETApplication

class SaveFinalesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val isReady = inputData.getBoolean("FINALES_READY", false)
        if (!isReady) return Result.failure()

        val control = inputData.getString("CONTROL") ?: return Result.failure()

        val prefs = applicationContext.getSharedPreferences("WorkerCache", Context.MODE_PRIVATE)
        val jsonFinales = prefs.getString("FINALES_TEMP", null) ?: return Result.failure()

        val app = applicationContext as SICENETApplication
        val localRepository = app.container.localRepository

        return try {
            val entity = CalifFinalEntity(
                control = control,
                jsonData = jsonFinales,
                ultimaActualizacion = System.currentTimeMillis()
            )

            localRepository.insertCalifFinal(entity)

            prefs.edit().remove("FINALES_TEMP").apply()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}