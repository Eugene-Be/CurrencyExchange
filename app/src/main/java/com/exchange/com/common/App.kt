package com.exchange.com.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

const val CURRENCY_SYMBOL_KEY = "CURRENCY_KEY"
const val API_KEY = "8370e5bf6bdf4ea89accb5dd26faabeb"
const val PREFS_KEY = "PREFS_KEY"
const val DATE_KEY = "DATE_KEY"

@HiltAndroidApp
class App : Application()