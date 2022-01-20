package com.exchange.com.data.remote.dto

data class ApiCurrencyRate(
    val base: String,
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val rates: Map<String, Double>,
)