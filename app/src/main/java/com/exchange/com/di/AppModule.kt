package com.exchange.com.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.exchange.com.common.PREFS_KEY
import com.exchange.com.data.local.database.RatesDatabase
import com.exchange.com.data.remote.OpenExchangeRatesApi
import com.exchange.com.data.repository.RatesRepositoryImpl
import com.exchange.com.domain.repository.RatesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideExchangeRatesApi(): OpenExchangeRatesApi {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl("https://openexchangerates.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideCurrencyDatabase(@ApplicationContext context: Context): RatesDatabase {
        return Room.databaseBuilder(context, RatesDatabase::class.java, "currencyRatesDb")
            .build()
    }

    @Provides
    fun getSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRatesRepository(
        api: OpenExchangeRatesApi,
        db: RatesDatabase,
        sharedPrefs: SharedPreferences,
    ): RatesRepository {
        return RatesRepositoryImpl(api, db, sharedPrefs)
    }
}