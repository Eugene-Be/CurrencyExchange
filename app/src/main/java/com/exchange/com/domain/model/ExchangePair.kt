package com.exchange.com.domain.model

data class ExchangePair(
    val base: String,
    val secondCurrency: String,
    val exchangeRate: String,
    val fluctuation: String,
    val direction: FluctuationDirection
)