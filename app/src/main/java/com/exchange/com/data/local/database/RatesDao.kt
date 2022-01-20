package com.exchange.com.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RatesDao {
    @Query("SELECT * FROM exchange_rates")
    suspend fun getRateEntityList(): List<RateEntity?>?

    @Query("SELECT * FROM exchange_rates WHERE symbol LIKE :symbol  || '%'")
    suspend fun getRateListBySearch(symbol: String): List<RateEntity?>?

    @Insert
    suspend fun insertRateEntity(rateEntity: RateEntity?)

    @Query("DELETE FROM exchange_rates")
    suspend fun deleteAllRates()
}