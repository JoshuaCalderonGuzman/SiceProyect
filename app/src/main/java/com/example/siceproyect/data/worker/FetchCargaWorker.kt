package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.siceproyect.SICENETApplication
import com.google.gson.Gson

class FetchCargaWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val app = applicationContext as SICENETApplication
        val repository = app.container.snRepository

        return try {
            val cargaList = repository.cargaAcademica() // Pide los datos a Retrofit

            if (cargaList.isNotEmpty()) {
                val jsonResult = Gson().toJson(cargaList)
                // Pasa los datos como salida para el Worker 2
                val outputData = workDataOf("CARGA_JSON" to jsonResult)
                Result.success(outputData)
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}