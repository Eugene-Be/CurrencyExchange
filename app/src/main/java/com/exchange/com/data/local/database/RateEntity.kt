package com.exchange.com.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
class RateEntity(val symbol:String, val name:String, val lastValue:Double, val pastValue:Double) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}