package com.exchange.com.domain.use_case

import com.exchange.com.common.Resource
import com.exchange.com.domain.model.ExchangePair
import com.exchange.com.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCurrencyPairsByNameUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) : GetCurrencyPairsUseCase(ratesRepository) {

    operator fun invoke(
        baseCurrency: String,
        secondCurrency: String
    ): Flow<Resource<List<ExchangePair>>> = flow {
        try {
            emit(Resource.Loading())

            val rates = ratesRepository.getCurrencyRate()
            val baseCurrencyRate = rates.find { it.symbol == baseCurrency }

            val list = exchangePairList(rates, baseCurrencyRate).filter {
                it.secondCurrency.contains(
                    secondCurrency,
                    true
                )
            }

            emit(Resource.Success(list))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message()))
        } catch (e: IOException) {
            emit(Resource.Error("No connection to server"))
        }
    }
}