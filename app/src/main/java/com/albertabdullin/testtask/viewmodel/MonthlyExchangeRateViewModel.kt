package com.albertabdullin.testtask.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import com.albertabdullin.testtask.database.CurrencyEntity
import com.albertabdullin.testtask.repository.CurrentMonthlyExchangeRateRepository
import com.albertabdullin.testtask.util.CurrencyExchangeRateCheckWorker
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class MonthlyExchangeRateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CurrentMonthlyExchangeRateRepository(application.applicationContext)
    private val _networkStateNotify = MutableLiveData<Boolean>()
    val networkStateNotify: LiveData<Boolean>
        get() = _networkStateNotify
    private val _refreshObserver = MutableLiveData<Boolean>()
    val refreshObserver: LiveData<Boolean>
        get() = _refreshObserver

    fun getExchangeRateForMonth(): LiveData<List<CurrencyEntity>> = repository.listOfExchangeRate

    init {
        getData()
        WorkManager.getInstance(application).enqueueUniquePeriodicWork(
            "checkingCurrentExchangeRate",
            ExistingPeriodicWorkPolicy.KEEP,
            getRequest(getConstraints())
        )
    }

    private fun getConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private fun getRequest(constraints: Constraints) =
        PeriodicWorkRequestBuilder<CurrencyExchangeRateCheckWorker>(1, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    fun getData() {
        viewModelScope.launch {
            try {
                repository.getCurrentMonthlyData()
            } catch (e: UnknownHostException) {
                _networkStateNotify.value = false
                _networkStateNotify.value = true
            }
            _refreshObserver.value = false
        }

    }

}