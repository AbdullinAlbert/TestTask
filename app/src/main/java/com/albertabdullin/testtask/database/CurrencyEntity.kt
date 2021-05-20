package com.albertabdullin.testtask.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo val date: String,
    @ColumnInfo val value: Double
)