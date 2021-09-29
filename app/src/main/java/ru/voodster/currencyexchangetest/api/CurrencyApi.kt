package ru.voodster.currencyexchangetest.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("/latest")
    fun getLatest(
        @Query("access_key")access_key:String,
        @Query("base") base:String,
        @Query("symbols") symbols:String
    ): Single<ApiModel>

    @GET("/latest")
    fun getLatestFlow(
        @Query("access_key")access_key:String,
        @Query("base") base:String,
        @Query("symbols") symbols:String
    ): Single<ApiModel>
}