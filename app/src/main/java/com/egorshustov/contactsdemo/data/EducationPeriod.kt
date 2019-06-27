package com.egorshustov.contactsdemo.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EducationPeriod(
    @SerializedName("start")
    @ColumnInfo(name = "start_date_time")
    val startDateTimeString: String?,
    @SerializedName("end")
    @ColumnInfo(name = "end_date_time")
    val endDateTimeString: String?,
    @ColumnInfo(name = "start_unix_seconds")
    var startUnixSeconds: Int?,
    @ColumnInfo(name = "end_unix_seconds")
    var endUnixSeconds: Int?
) : Parcelable