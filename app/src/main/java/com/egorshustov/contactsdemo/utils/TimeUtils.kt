package com.egorshustov.contactsdemo.utils

import java.util.*

object TimeUtils {
    const val MILLISECONDS_IN_SECOND = 1000

    fun getCurrentTimeInUnixMillis(): Long {
        return (Calendar.getInstance(TimeZone.getDefault()).timeInMillis)
    }
}