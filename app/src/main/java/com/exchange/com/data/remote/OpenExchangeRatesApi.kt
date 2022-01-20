package com.exchange.com.data.remote

import com.exchange.com.common.API_KEY
import com.exchange.com.data.remote.dto.ApiCurrencyRate
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenExchangeRatesApi {
    @GET("currencies.json?")
    suspend fun getCurrencies(): Map<String, String>

    @GET("latest.json?app_id=$API_KEY")
    suspend fun getLatestRates(): ApiCurrencyRate

    @GET("historical/{date}.json?app_id=$API_KEY")
    suspend fun getRatesByDate(@Path("date") date: String): ApiCurrencyRate
}