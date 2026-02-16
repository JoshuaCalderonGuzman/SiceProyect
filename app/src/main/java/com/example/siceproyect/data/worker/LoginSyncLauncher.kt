package com.example.siceproyect.data.worker

import android.content.Context
import androidx.work.*

fun lanzarLoginSync(context: Context, control: String, nip: String) {

    val login = OneTimeWorkRequestBuilder<LoginSyncWorker>()
        .setInputData(workDataOf("control" to control, "nip" to nip))
        .build()

    val save = OneTimeWorkRequestBuilder<SaveLoginWorker>().build()

    WorkManager.getInstance(context)
        .beginUniqueWork(
            "login_sync",
            ExistingWorkPolicy.REPLACE,
            login
        )
        .then(save)
        .enqueue()
}
