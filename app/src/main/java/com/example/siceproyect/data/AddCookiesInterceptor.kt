package com.example.siceproyect.data

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class AddCookiesInterceptor(// We're storing our stuff in a database made just for cookies called PREF_COOKIES.
    // I reccomend you do this, and don't change this default value.
    private val context: Context
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val cookies = context
            .getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE)
            .getStringSet("cookies", emptySet()) ?: emptySet()

        for (cookie in cookies) {
            builder.addHeader("Cookie", cookie)
        }
        return chain.proceed(builder.build())
    }


}
