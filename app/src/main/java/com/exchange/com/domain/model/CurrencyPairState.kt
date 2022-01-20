package com.exchange.com.domain.model

data class CurrencyPairState(
    val isLoading: Boolean = false,
    val currencyPairList: List<ExchangePair> = emptyList(),
    val error: String = "",
)
