package com.albertabdullin.testtask.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.RuntimeException

class MonthlyExchangeRateViewModelFactory(private val application: Application):
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthlyExchangeRateViewModel::class.java))
            return MonthlyExchangeRateViewModel(application) as T
        else throw IllegalArgumentException("Unknown ViewModel class")
    }

}