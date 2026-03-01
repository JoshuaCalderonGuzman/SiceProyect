package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.siceproyect.SICENETApplication
import com.google.gson.Gson

class FetchUnidadesWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val app = applicationContext as SICENETApplication
        val repository = app.container.snRepository

        val control = inputData.getString("CONTROL") ?: return Result.failure()

        return try {
            val unidades = repository.califUnidades()

            if (unidades != null && unidades.isNotEmpty()) {
                val jsonResult = Gson().toJson(unidades)

                val prefs = applicationContext.getSharedPreferences("WorkerCache", Context.MODE_PRIVATE)
                prefs.edit().putString("UNIDADES_TEMP", jsonResult).apply()

                val outputData = workDataOf("UNIDADES_READY" to true, "CONTROL" to control)
                Result.success(outputData)
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}