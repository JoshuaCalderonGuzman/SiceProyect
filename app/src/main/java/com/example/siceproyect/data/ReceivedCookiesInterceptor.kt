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

        val setCookieHeaders = originalResponse.headers("Set-Cookie")
        if (setCookieHeaders.isNotEmpty()) {
            val prefs = context.getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE)

            val existingCookies = prefs.getStringSet("cookies", emptySet())?.toMutableSet() ?: mutableSetOf()


            existingCookies.addAll(setCookieHeaders)

            prefs.edit {
                putStringSet("cookies", existingCookies)
                apply()
            }
        }
        return originalResponse
    }
}