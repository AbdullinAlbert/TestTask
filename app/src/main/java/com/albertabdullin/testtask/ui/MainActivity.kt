package com.albertabdullin.testtask.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.albertabdullin.testtask.R
import com.albertabdullin.testtask.viewmodel.MonthlyExchangeRateViewModel
import com.albertabdullin.testtask.databinding.ActivityMainRefreshBinding
import com.albertabdullin.testtask.util.CurrencyAdapter
import com.albertabdullin.testtask.viewmodel.MonthlyExchangeRateViewModelFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainRefreshBinding.inflate(layoutInflater)
        val refreshLayout = binding.root
        setContentView(refreshLayout)
        val viewModelFactory = MonthlyExchangeRateViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MonthlyExchangeRateViewModel::class.java)
        refreshLayout.setOnRefreshListener {
            viewModel.getData()
        }
        val divider = DividerItemDecoration(binding.recyclerview.context,
            DividerItemDecoration.VERTICAL)
        val adapter = CurrencyAdapter()
        binding.recyclerview.addItemDecoration(divider)
        binding.recyclerview.adapter = adapter
        viewModel.getExchangeRateForMonth().observe(this) {
            it?.let {
                adapter.submitList(it)
            }
        }
        viewModel.networkStateNotify.observe(this) {
            it?.let {
                if (!it) Toast.makeText(
                        this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
            if (refreshLayout.isRefreshing) refreshLayout.isRefreshing = false
        }
        viewModel.refreshObserver.observe(this) {
            it?.let {
                if (refreshLayout.isRefreshing) refreshLayout.isRefreshing = it
            }
        }
        createNotificationChannel()
    }
    //some text for test #2
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notifier_about_height_dollar_exchange_rate)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                    getString(R.string.height_dollar_exchange_rate_id), name, importance
            )
            with(NotificationManagerCompat.from(this)) {
                createNotificationChannel(channel)
            }
        }
    }

}