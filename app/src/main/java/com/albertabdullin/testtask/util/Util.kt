package com.albertabdullin.testtask.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun getStringViewOfCurrency(value: Double): String {
    val nf: NumberFormat = NumberFormat.getCurrencyInstance(Locale("ru","RU"))
    return nf.format(value)
}

fun getStringViewOfDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

fun convertStringToDouble(s: String): Double = s.replace(',', '.', ignoreCase = true).toDouble()
