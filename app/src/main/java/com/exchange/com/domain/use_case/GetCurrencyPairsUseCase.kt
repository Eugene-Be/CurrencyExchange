package com.exchange.com.domain.use_case

import com.exchange.com.common.Resource
import com.exchange.com.domain.model.CurrencyRate
import com.exchange.com.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import com.exchange.com.domain.model.ExchangePair
import com.exchange.com.domain.model.FluctuationDirection

open class GetCurrencyPairsUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {
    internal operator fun invoke(baseCurrency: String): Flow<Resource<List<ExchangePair>>> = flow {
        try {
            emit(Resource.Loading())

            val rates = ratesRepository.getCurrencyRate()
            val baseCurrencyRate = rates.find { it.symbol == baseCurrency }
            val list = exchangePairList(rates, baseCurrencyRate)

            emit(Resource.Success(list))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message()))
        } catch (e: IOException) {
            emit(Resource.Error("No connection to server"))
        }
    }

    internal fun exchangePairList(
        rates: List<CurrencyRate>,
        baseCurrencyRate: CurrencyRate?
    ): List<ExchangePair> {
        val list = rates.map {
            val fluctuation = getFluctuation(it.latestValue, it.pastValue)
            ExchangePair(
                baseCurrencyRate!!.symbol,
                it.symbol,
                String.format("%.4f", it.latestValue / baseCurrencyRate.latestValue),
                String.format("%.2f", fluctuation),
                getFluctuationDirection(fluctuation),
            )
        }
        return list
    }

    private fun getFluctuationDirection(fluctuation: Double): FluctuationDirection {
        return when (true) {
            fluctuation > 0 -> {
                FluctuationDirection.UP
            }
            fluctuation < 0 -> {
                FluctuationDirection.DOWN
            }
            else -> {
                FluctuationDirection.NONE
            }
        }
    }

    private fun getFluctuation(latest: Double, old: Double): Double {
        return (latest - old) / old * 100
    }
}

