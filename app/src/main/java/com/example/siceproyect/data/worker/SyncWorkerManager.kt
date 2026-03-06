package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

fun lanzarSyncCargaAcademica(context: Context, matriculaControl: String) {
    val fetchRequest = OneTimeWorkRequestBuilder<FetchCargaWorker>()
        .addTag("FETCH_CARGA_TAG") //Importante para monitorearlo en la UI
        .build()

    val saveRequest = OneTimeWorkRequestBuilder<SaveCargaWorker>()
        .setInputData(Data.Builder().putString("CONTROL", matriculaControl).build())
        .build()

    WorkManager.getInstance(context)
        .beginUniqueWork(
            "sync_carga_$matriculaControl",
            ExistingWorkPolicy.REPLACE,
            fetchRequest
        )
        .then(saveRequest) //El Worker 1 le pasa datos al Worker 2 automáticamente
        .enqueue()
}

fun lanzarSyncKardex(context: Context, matriculaControl: String, modEducativo: String) {
    val fetchRequest = OneTimeWorkRequestBuilder<FetchKardexWorker>()
        .addTag("FETCH_KARDEX_TAG")
        .setInputData(androidx.work.Data.Builder().putString("MOD_EDUCATIVO", modEducativo).build())
        .build()

    val saveRequest = OneTimeWorkRequestBuilder<SaveKardexWorker>()
        .setInputData(androidx.work.Data.Builder().putString("CONTROL", matriculaControl).build())
        .build()

    WorkManager.getInstance(context)
        .beginUniqueWork("sync_kardex_$matriculaControl", ExistingWorkPolicy.REPLACE, fetchRequest)
        .then(saveRequest)
        .enqueue()
}

// PARA CALIFICACIONES FINALES
fun lanzarSyncFinales(context: Context, matriculaControl: String, modEducativo: String) {
    val fetchRequest = OneTimeWorkRequestBuilder<FetchFinalesWorker>()
        .setInputData(androidx.work.Data.Builder()
            .putString("MOD_EDUCATIVO", modEducativo)
            .putString("CONTROL", matriculaControl)
            .build()
        )
        .build()

    val saveRequest = OneTimeWorkRequestBuilder<SaveFinalesWorker>()
        .addTag("SAVE_FINALES_TAG")
        .build()

    WorkManager.getInstance(context)
        .beginUniqueWork("sync_finales_$matriculaControl", ExistingWorkPolicy.REPLACE, fetchRequest)
        .then(saveRequest)
        .enqueue()
}

//PARA CALIFICACIONES POR UNIDADES
fun lanzarSyncUnidades(context: Context, matriculaControl: String) {
    val fetchRequest = OneTimeWorkRequestBuilder<FetchUnidadesWorker>()
        .setInputData(androidx.work.Data.Builder()
            .putString("CONTROL", matriculaControl)
            .build()
        )
        .build()

    val saveRequest = OneTimeWorkRequestBuilder<SaveUnidadesWorker>()
        .addTag("SAVE_UNIDADES_TAG")
        .build()

    WorkManager.getInstance(context)
        .beginUniqueWork("sync_unidades_$matriculaControl", ExistingWorkPolicy.REPLACE, fetchRequest)
        .then(saveRequest)
        .enqueue()
}