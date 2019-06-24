package com.egorshustov.contactsdemo.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    const val MILLISECONDS_IN_SECOND = 1000

    fun getCurrentTimeInUnixMillis(): Long {
        return (Calendar.getInstance(TimeZone.getDefault()).timeInMillis)
    }

    fun timeStringToUnixSeconds(pattern: String, timeString: String?): Int? {
        timeString ?: return null
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.time = SimpleDateFormat(pattern, Locale.getDefault()).parse(timeString)
        return (calendar.timeInMillis / MILLISECONDS_IN_SECOND).toInt()
    }
}