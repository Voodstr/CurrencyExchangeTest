package ru.voodster.currencyexchangetest.api

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.voodster.currencyexchangetest.App
import javax.inject.Singleton

@Module
class ApiModule {
    @Singleton
    @Provides
    fun provideApi(): CurrencyApi {

        val okHttpClient = OkHttpClient.Builder()
            .build()

        val rxAdapter = RxJava3CallAdapterFactory.create()

        return Retrofit.Builder()
            .baseUrl(App.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(rxAdapter)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(CurrencyApi::class.java)
    }
}