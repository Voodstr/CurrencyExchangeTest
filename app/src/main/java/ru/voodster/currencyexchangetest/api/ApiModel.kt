package ru.voodster.currencyexchangetest.api

import com.google.gson.annotations.SerializedName

data class ApiModel(
    @SerializedName("success") var success: Boolean = true,
    @SerializedName("timestamp") var timestamp: Long = 1632902044,
    @SerializedName("base") var base: String = "EUR",
    @SerializedName("date") var date: String = "2021-09-29",
    @SerializedName("rates") var rates: Map<String,Float>  = mapOf()
)

