package com.egorshustov.contactsdemo.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey
    val id: String,
    val name: String?,
    val phone: String?,
    val height: Float?,
    val biography: String?,
    val temperament: String?,
    @Embedded
    val educationPeriod: EducationPeriod?,
    @ColumnInfo(name = "fetch_time_unix_millis")
    var fetchTimeUnixMillis: Long?
)