package com.app.weather.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.getDateFromTimeStamp() : String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val cal = Calendar.getInstance()
    cal.timeInMillis = this.toLong() * 1000L

    return sdf.format(cal.time)
}