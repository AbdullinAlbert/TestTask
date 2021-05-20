package com.albertabdullin.testtask.util


import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.albertabdullin.testtask.R
import com.albertabdullin.testtask.database.CurrencyDao
import com.albertabdullin.testtask.database.CurrencyEntity
import com.albertabdullin.testtask.database.ExchangeRateDataBase
import com.albertabdullin.testtask.network.DollarRateAPI.dollarRateService
import com.albertabdullin.testtask.network.transformToDataBaseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

const val TITLE_TEXT_KEY = "titleText key"
const val TEXT_CONTEXT_KEY = "textContext key"

class CurrencyExchangeRateCheckWorker(
    private val context: Context,

    params: WorkerParameters): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        var dataBase: CurrencyDao?
        var currencyEntityFromNetwork: CurrencyEntity?
        var currencyEntityFromDB: CurrencyEntity?
        val calendar = Calendar.getInstance()
        val today = getStringViewOfDate(calendar)
        val map = mapOf("date_req1" to today, "date_req2" to today, "VAL_NM_RQ" to "R01235")
        try {
            withContext(Dispatchers.IO) {
                currencyEntityFromNetwork = dollarRateService.getDollarRateForCertainPeriod(map)
                    .transformToDataBaseEntity()[0]
                dataBase = ExchangeRateDataBase.getInstance(context).currencyDao
                currencyEntityFromDB = dataBase!!.getRecentValueOfExchangeRate()
            }
        } catch (e: Exception) {
            return Result.failure()
        }
        if (currencyEntityFromDB == null || currencyEntityFromNetwork == null)
            return Result.failure()
        if (currencyEntityFromNetwork!!.value > currencyEntityFromDB!!.value) {
            val currencyEntity: CurrencyEntity =
                if (currencyEntityFromNetwork!!.date == currencyEntityFromDB!!.date)
                    CurrencyEntity(
                        currencyEntityFromDB!!.id,
                        currencyEntityFromDB!!.date,
                        currencyEntityFromNetwork!!.value)
                else CurrencyEntity(
                    currencyEntityFromDB!!.id + 1,
                        currencyEntityFromDB!!.date,
                        currencyEntityFromNetwork!!.value)
            try {
                withContext(Dispatchers.IO) {
                    dataBase!!.insert(currencyEntity)
                }
            } catch (e: Exception) {
                return Result.failure()
            }
            showNotification(currencyEntityFromNetwork!!.value, currencyEntityFromDB!!.value)
        }
        return Result.success()
    }

    private fun showNotification(value1: Double, value2: Double) {
        val difference = getStringViewOfCurrency(value1 - value2)
        val titleText = context.getString(R.string.dollar_exchange_rate_is_up_on, difference)
        val currentConst = getStringViewOfCurrency(value1)
        val textContent = context.getString(R.string.for_now_its_cost_is, currentConst)
        Intent(context, NotificationBroadcastReceiver::class.java).also { intent ->
            intent.setAction("com.albertabdullin.testtask.MY_NOTIFICATION")
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            intent.putExtra(TITLE_TEXT_KEY, titleText)
            intent.putExtra(TEXT_CONTEXT_KEY, textContent)
            context.sendBroadcast(intent)
        }
    }

}