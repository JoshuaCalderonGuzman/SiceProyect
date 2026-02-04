package com.example.siceproyect.data

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ReceivedCookiesInterceptor // AddCookiesInterceptor()
    (private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = originalResponse.headers("Set-Cookie")
            val sharedPref = context.getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE).edit()
            sharedPref.putStringSet("cookies", cookies.toSet()).apply()
        }
        return originalResponse
    }
}