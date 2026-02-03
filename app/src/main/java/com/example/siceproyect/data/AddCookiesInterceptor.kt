package com.example.siceproyect.data

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class AddCookiesInterceptor(// We're storing our stuff in a database made just for cookies called PREF_COOKIES.
    // I reccomend you do this, and don't change this default value.
    private val context: Context
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(context)
            .getStringSet(PREF_COOKIES, emptySet())

        preferences?.forEach { cookie ->
            builder.addHeader("Cookie", cookie)
        }
        return chain.proceed(builder.build())
    }

    companion object {
        const val PREF_COOKIES = "PREF_COOKIES"
    }
}
