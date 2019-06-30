package com.egorshustov.contactsdemo.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    const val MILLISECONDS_IN_SECOND = 1000

    fun getCurrentTimeInUnixMillis(): Long {
        return (Calendar.getInstance(TimeZone.getDefault()).timeInMillis)
    }

    fun unixMillisToDateString(unixMillis: Long?): String? {
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(unixMillis)
    }
}