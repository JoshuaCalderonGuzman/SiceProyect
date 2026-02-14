package com.example.siceproyect.data

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import org.json.JSONArray

class PersistentCookieJar(
    private val context: Context
) : CookieJar {

    companion object {
        private const val PREF_COOKIES = "CookiePrefs"
        private const val KEY_COOKIES = "cookies"
    }

    private val cache = HashMap<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {

        cache[url.host] = cookies

        val prefs = context.getSharedPreferences(PREF_COOKIES, Context.MODE_PRIVATE)

        val jsonArray = JSONArray()
        cookies.forEach {
            jsonArray.put(it.toString())
        }

        prefs.edit().putString(KEY_COOKIES, jsonArray.toString()).apply()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {

        cache[url.host]?.let { return it }

        val prefs = context.getSharedPreferences(PREF_COOKIES, Context.MODE_PRIVATE)
        val stored = prefs.getString(KEY_COOKIES, null) ?: return emptyList()

        val jsonArray = JSONArray(stored)
        val cookies = mutableListOf<Cookie>()

        for (i in 0 until jsonArray.length()) {
            Cookie.parse(url, jsonArray.getString(i))?.let {
                cookies.add(it)
            }
        }

        cache[url.host] = cookies

        return cookies
    }
}
