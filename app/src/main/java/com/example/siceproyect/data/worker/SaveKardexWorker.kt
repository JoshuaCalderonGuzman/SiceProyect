package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.siceproject.data.local.entities.KardexEntity
import com.example.siceproyect.SICENETApplication

class SaveKardexWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // Validamos que el primer Worker haya terminado exitosamente
        val isReady = inputData.getBoolean("KARDEX_READY", false)
        if (!isReady) return Result.failure()

        val control = inputData.getString("CONTROL") ?: return Result.failure()

        // 🔥 RECUPERAMOS EL JSON GIGANTE DEL CACHÉ
        val prefs = applicationContext.getSharedPreferences("WorkerCache", Context.MODE_PRIVATE)
        val jsonKardex = prefs.getString("KARDEX_TEMP", null) ?: return Result.failure()

        val app = applicationContext as SICENETApplication
        val localRepository = app.container.localRepository

        return try {
            val entity = KardexEntity(
                control = control,
                jsonData = jsonKardex,
                ultimaActualizacion = System.currentTimeMillis()
            )
            localRepository.insertKardex(entity)

            // Limpiamos el caché temporal
            prefs.edit().remove("KARDEX_TEMP").apply()

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}