package com.exchange.com.domain.repository

import com.exchange.com.domain.model.CurrencyRate

interface RatesRepository {
    suspend fun getCurrencyRate(): List<CurrencyRate>
    suspend fun getCurrencyByName(symbol: String):List<CurrencyRate>
}