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

class DefaultAppContainer(applicationContext: Context) : com.example.siceproyect.data.AppContainer {
    private val baseUrlSN = "https://sicenet.surguanajuato.tecnm.mx"
    private var client: OkHttpClient
    init {
        client = OkHttpClient()
        val builder = OkHttpClient.Builder()

        builder.addInterceptor(AddCookiesInterceptor(applicationContext)) // VERY VERY IMPORTANT

        builder.addInterceptor(ReceivedCookiesInterceptor(applicationContext)) // VERY VERY IMPORTANT

        client = builder.build()
    }


    private val retrofitSN: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrlSN)
        .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
        .client(client)
        .build()

    //bodyacceso.toRequestBody("text/xml; charset=utf-8".toMediaType())


    private val retrofitServiceSN: SICENETWService by lazy {
        retrofitSN.create(SICENETWService::class.java)
    }

    override val snRepository: NetworSNRepository by lazy {
        NetworSNRepository(retrofitServiceSN)
    }
}