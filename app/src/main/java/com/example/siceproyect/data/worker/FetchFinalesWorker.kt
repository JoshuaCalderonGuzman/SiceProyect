package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.siceproyect.SICENETApplication
import com.google.gson.Gson

class FetchFinalesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val app = applicationContext as SICENETApplication
        val repository = app.container.snRepository

        val modEducativo = inputData.getString("MOD_EDUCATIVO") ?: return Result.failure()
        val control = inputData.getString("CONTROL") ?: return Result.failure()

        return try {
            val finales = repository.califFinal(modEducativo.toInt())

            // Como las calificaciones son directamente una Lista, validamos con isNotEmpty()
            if (finales != null && finales.isNotEmpty()) {
                val jsonResult = Gson().toJson(finales)

                val prefs = applicationContext.getSharedPreferences("WorkerCache", Context.MODE_PRIVATE)
                prefs.edit().putString("FINALES_TEMP", jsonResult).apply()

                val outputData = workDataOf("FINALES_READY" to true, "CONTROL" to control)
                Result.success(outputData)
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}