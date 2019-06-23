package com.egorshustov.contactsdemo.data

import androidx.room.ColumnInfo

data class EducationPeriod(
    @ColumnInfo(name = "start_date_time")
    val startDateTimeString: String?,
    @ColumnInfo(name = "end_date_time")
    val endDateTimeString: String?,
    @ColumnInfo(name = "start_unix_seconds")
    var startUnixSeconds: Int?,
    @ColumnInfo(name = "end_unix_seconds")
    var endUnixSeconds: Int?
)

