package com.albertabdullin.testtask.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.albertabdullin.testtask.database.CurrencyEntity
import com.albertabdullin.testtask.database.ExchangeRateDataBase
import com.albertabdullin.testtask.network.DollarRateAPI
import com.albertabdullin.testtask.network.transformToDataBaseEntity
import com.albertabdullin.testtask.util.getStringViewOfDate

import java.util.*

class CurrentMonthlyExchangeRateRepository(private val context: Context) {

    private val dataBase = ExchangeRateDataBase.getInstance(context).currencyDao
    private val dollarRateService = DollarRateAPI.dollarRateService

    private val _listOfExchangeRate = liveData {
        val list = dataBase.getAllData()
        emit(list)
    }

    val listOfExchangeRate: LiveData<List<CurrencyEntity>>
        get() = _listOfExchangeRate

    suspend fun getCurrentMonthlyData() {
        val calendar = Calendar.getInstance()
        val end = getStringViewOfDate(calendar)
        calendar.add(Calendar.MONTH, -1)
        val begin = getStringViewOfDate(calendar)
        val map = mapOf("date_req1" to begin, "date_req2" to end, "VAL_NM_RQ" to "R01235")
        val valCurs = dollarRateService.getDollarRateForCertainPeriod(map)
        dataBase.deleteAllData()
        dataBase.insertAll(valCurs.transformToDataBaseEntity())
    }
}