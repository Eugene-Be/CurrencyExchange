package com.exchange.com.domain.model

data class CurrencyRate(
    val symbol: String,
    val name: String,
    val latestValue: Double,
    val pastValue: Double,
)