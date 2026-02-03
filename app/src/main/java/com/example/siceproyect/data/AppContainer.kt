package com.example.siceproyect.data

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.example.siceproyect.network.SICENETWService
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

interface AppContainer {
    val snRepository: SNRepository
}

class DefaultAppContainer(applicationContext: Context) : AppContainer {
    private val baseUrlSN = "https://sicenet.surguanajuato.tecnm.mx"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AddCookiesInterceptor(applicationContext))
        .addInterceptor(ReceivedCookiesInterceptor(applicationContext))
        .build()

    private val retrofitSN = Retrofit.Builder()
        .baseUrl(baseUrlSN)
        .client(client) // Usamos el cliente con interceptores
        // Eliminamos conversores por ahora ya que usas ResponseBody manual
        .build()

    private val retrofitServiceSN: SICENETWService by lazy {
        retrofitSN.create(SICENETWService::class.java)
    }

    override val snRepository: SNRepository by lazy {
        NetworSNRepository(retrofitServiceSN)
    }
}