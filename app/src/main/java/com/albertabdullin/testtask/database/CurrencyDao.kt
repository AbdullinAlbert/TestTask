package com.albertabdullin.testtask.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency ORDER BY id DESC")
    fun getAllData(): LiveData<List<CurrencyEntity>>

    @Query("SELECT * FROM currency ORDER BY id DESC LIMIT 1")
    suspend fun getRecentValueOfExchangeRate(): CurrencyEntity

    @Query("DELETE FROM currency")
    suspend fun deleteAllData()

    @Insert
    suspend fun insertAll(currencyEntityList: List<CurrencyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyEntity: CurrencyEntity)
}