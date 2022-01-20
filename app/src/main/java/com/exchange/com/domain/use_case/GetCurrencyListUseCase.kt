package com.exchange.com.domain.use_case

import android.util.Log
import com.exchange.com.common.Resource
import com.exchange.com.domain.model.CurrencyRate
import com.exchange.com.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject

class GetCurrencyListUseCase @Inject constructor(
    private val ratesRepository: RatesRepository
) {
    operator fun invoke(): Flow<Resource<List<CurrencyRate>>> = flow {
        try {
            emit(Resource.Loading())

            val list = ratesRepository.getCurrencyRate()

            emit(Resource.Success(list))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message()))
        } catch (e: IOException) {
            emit(Resource.Error("No connection to server"))
        }
    }
}