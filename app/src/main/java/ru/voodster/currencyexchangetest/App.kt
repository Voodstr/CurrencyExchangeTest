package ru.voodster.currencyexchangetest

import android.app.Application
import android.util.Log

class App:Application() {


    companion object{
        const val BASE_URL = "http://api.exchangeratesapi.io/v1/"
        const val TAG = "App"
        var instance: App? = null
            private set
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }

}