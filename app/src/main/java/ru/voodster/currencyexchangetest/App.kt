package ru.voodster.currencyexchangetest

import android.app.Application

class App:Application() {


    companion object{
        const val BASE_URL = "http://api.exchangeratesapi.io/v1/"
        var instance: App? = null
            private set
    }

    init {
        instance = this
    }

}