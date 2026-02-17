package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.*
import com.example.siceproyect.data.DefaultAppContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginSyncWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val control = inputData.getString("control")!!
            val nip = inputData.getString("nip")!!

            // 🔹 Crear el AppContainer real de tu app
            val container = DefaultAppContainer(applicationContext)

            // 🔹 Obtener el repositorio
            val repo = container.snRepository

            // 🔹 Paso 1: hacer login
            val loginResult = repo.acceso(control, nip)

            if (!loginResult.success) {
                return@withContext Result.failure()
            }

            // 🔹 Paso 2: obtener datos del alumno
            val alumno = repo.alumnoDatos()


            Result.success(
                workDataOf(
                    "control" to alumno.matricula,   // ← CORREGIDO
                    "nombre" to alumno.nombre,
                    "carrera" to alumno.carrera
                )
            )

        } catch (e: Exception) {
            Result.failure()
        }
    }
}
