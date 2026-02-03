package com.example.siceproyect

import android.app.Application
import com.example.siceproyect.data.AppContainer
import com.example.siceproyect.data.DefaultAppContainer

class SICENETApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}