package com.albertabdullin.testtask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CurrencyEntity::class], version = 1, exportSchema = false)
abstract class ExchangeRateDataBase: RoomDatabase() {
    abstract val currencyDao: CurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: ExchangeRateDataBase? = null
        fun getInstance(context: Context): ExchangeRateDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        ExchangeRateDataBase::class.java,
                        "exchange_rate_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

