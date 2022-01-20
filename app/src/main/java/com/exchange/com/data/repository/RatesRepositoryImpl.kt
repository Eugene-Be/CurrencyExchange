package com.exchange.com.data.repository

import android.content.SharedPreferences
import com.exchange.com.common.DATE_KEY
import com.exchange.com.data.local.database.RateEntity
import com.exchange.com.data.local.database.RatesDatabase
import com.exchange.com.data.remote.OpenExchangeRatesApi
import com.exchange.com.domain.model.CurrencyRate
import com.exchange.com.domain.repository.RatesRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
    private val api: OpenExchangeRatesApi,
    private val db: RatesDatabase,
    private val sharedPrefs: SharedPreferences
) : RatesRepository {
    private val dayMilisecond = 86400000L
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault(Locale.Category.FORMAT))
    private val currentDate: String = sdf.format(Date())

    override suspend fun getCurrencyRate(): List<CurrencyRate> {
        val currencyRateList: List<CurrencyRate>

        if (isActualData()) {
            currencyRateList = getFromLocalDb()!!
        } else {
            currencyRateList = fetchCurrencyRateList()
            db.dao.deleteAllRates()
            currencyRateList.forEach { db.dao.insertRateEntity(it.toRateEntity()) }

            sharedPrefs.edit().putString(DATE_KEY, currentDate).apply()
        }

        return currencyRateList
    }

    private suspend fun getFromLocalDb(): List<CurrencyRate>? {
        return db.dao.getRateEntityList()?.let { rateEntities ->
            rateEntities.map { it!!.toCurrencyRate() }
        }
    }

    private suspend fun fetchCurrencyRateList(): List<CurrencyRate> {
        val names = api.getCurrencies()
        val latestRates = api.getLatestRates()
        val pastRates = api.getRatesByDate(getYesterdayDate()).rates

        val currencyRateList = latestRates.rates.entries.map {
            CurrencyRate(
                it.key,
                names[it.key] ?: "",
                it.value,
                pastRates[it.key]!!
            )
        }

        return currencyRateList
    }

    override suspend fun getCurrencyByName(symbol: String): List<CurrencyRate> {
        return db.dao.getRateListBySearch(symbol)?.map { it!!.toCurrencyRate() }!!
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = Date().time - dayMilisecond }
        return sdf.format(calendar.time)
    }

    private fun isActualData(): Boolean {
        sharedPrefs.getString(DATE_KEY, null)?.let { savedDate ->
            return savedDate == currentDate
        } ?: return false
    }
}

fun RateEntity.toCurrencyRate(): CurrencyRate {
    return CurrencyRate(
        this.symbol,
        this.name,
        this.lastValue,
        this.pastValue,
    )
}

fun CurrencyRate.toRateEntity(): RateEntity {
    return RateEntity(
        this.symbol,
        this.name,
        this.latestValue,
        this.pastValue,
    )
}