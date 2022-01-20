package com.exchange.com.domain.model

data class CurrencyListState(
    val isLoading: Boolean = false,
    val currencyList: List<CurrencyRate> = emptyList(),
    val error: String = "",
)
