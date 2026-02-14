package com.example.siceproyect.data

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.example.siceproyect.network.SICENETWService
import retrofit2.converter.scalars.ScalarsConverterFactory
interface AppContainer {
    val snRepository: SNRepository
}

class DefaultAppContainer(
    applicationContext: Context
) : AppContainer {

    private val baseUrlSN = "https://sicenet.surguanajuato.tecnm.mx/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private val cookieJar = PersistentCookieJar(applicationContext)

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Connection", "Keep-Alive")
                    .build()
                chain.proceed(request)
            }

            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrlSN)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private val service: SICENETWService by lazy {
        retrofit.create(SICENETWService::class.java)
    }

    override val snRepository: SNRepository by lazy {
        NetworSNRepository(service)
    }
}
