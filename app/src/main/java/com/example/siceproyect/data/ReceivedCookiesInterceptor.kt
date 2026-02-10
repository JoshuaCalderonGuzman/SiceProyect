package com.example.siceproyect.data

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import androidx.core.content.edit

class ReceivedCookiesInterceptor // AddCookiesInterceptor()
    (private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = originalResponse.headers("Set-Cookie")
            context.getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE).edit {
                putStringSet("cookies", cookies.toSet())
            }
        }
        return originalResponse
    }
}