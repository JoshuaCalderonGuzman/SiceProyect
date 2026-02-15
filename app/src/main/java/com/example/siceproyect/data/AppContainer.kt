package com.example.siceproyect.data

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.example.siceproyect.network.SICENETWService
import okhttp3.logging.HttpLoggingInterceptor

interface AppContainer {
    val snRepository: SNRepository
}

class DefaultAppContainer(applicationContext: Context) : AppContainer {
    private val baseUrlSN = "https://sicenet.surguanajuato.tecnm.mx"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS // Importante: ver los Headers
        })
        .addInterceptor(AddCookiesInterceptor(applicationContext))
        .addInterceptor(ReceivedCookiesInterceptor(applicationContext))
        .build()

    private val retrofitSN = Retrofit.Builder()
        .baseUrl(baseUrlSN)
        .client(client)
        .build()

    private val retrofitServiceSN: SICENETWService by lazy {
        retrofitSN.create(SICENETWService::class.java)
    }

    override val snRepository: SNRepository by lazy {
        NetworSNRepository(retrofitServiceSN)
    }
}