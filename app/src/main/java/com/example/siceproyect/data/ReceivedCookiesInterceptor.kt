package com.example.siceproyect.data

import android.content.Context
import android.preference.PreferenceManager
import com.example.siceproyect.data.AddCookiesInterceptor.Companion.PREF_COOKIES
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ReceivedCookiesInterceptor // AddCookiesInterceptor()
    (private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val cookies = prefs.getStringSet(PREF_COOKIES, emptySet())?.toMutableSet()
                ?: mutableSetOf()

            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }

            prefs.edit()
                .putStringSet(PREF_COOKIES, cookies)
                .apply()

        }
        return originalResponse
    }
}