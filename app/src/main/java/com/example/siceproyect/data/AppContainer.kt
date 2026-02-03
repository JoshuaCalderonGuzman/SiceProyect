package com.example.siceproyect.data

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.example.siceproyect.network.SICENETWService

interface AppContainer {
    val snRepository: SNRepository
}

class DefaultAppContainer(
    private val context: Context
) : AppContainer {

    private val baseUrlSN = "https://sicenet.surguanajuato.tecnm.mx/"

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(SimpleCookieJar())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrlSN)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val snService = retrofit.create(SICENETWService::class.java)

    override val snRepository: SNRepository =
        NetworkSNRepository(snService)
}