package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.siceproyect.SICENETApplication
import com.google.gson.Gson

class FetchKardexWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val app = applicationContext as SICENETApplication
        val repository = app.container.snRepository

        val modEducativo = inputData.getString("MOD_EDUCATIVO") ?: return Result.failure()

        return try {
            val kardex = repository.kardex(modEducativo.toInt())
            if (kardex != null) {
                val jsonResult = Gson().toJson(kardex)

                //Guardamos en caché temporal
                val prefs = applicationContext.getSharedPreferences("WorkerCache", Context.MODE_PRIVATE)
                prefs.edit().putString("KARDEX_TEMP", jsonResult).apply()

                //Le mandamos al siguiente Worker la señal de que ya está listo
                val outputData = workDataOf("KARDEX_READY" to true)
                Result.success(outputData)
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}