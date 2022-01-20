package com.exchange.com.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RateEntity::class], version = 1, exportSchema = false)
abstract class RatesDatabase: RoomDatabase() {
    abstract val dao:RatesDao
}