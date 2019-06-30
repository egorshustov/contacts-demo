package com.egorshustov.contactsdemo.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "contacts")
@Parcelize
data class Contact(
    @PrimaryKey
    val id: String,
    val name: String?,
    val phone: String?,
    val height: Float?,
    val biography: String?,
    val temperament: String?,
    @ColumnInfo(name = "education_start_unix_millis")
    var educationStartUnixMillis: Long?,
    @ColumnInfo(name = "education_end_unix_millis")
    var educationEndUnixMillis: Long?,
    @ColumnInfo(name = "fetch_time_unix_millis")
    var fetchTimeUnixMillis: Long?
) : Parcelable {
    fun contentsEquals(otherContact: Contact): Boolean {
        return name == otherContact.name &&
                phone == otherContact.phone &&
                height == otherContact.height
    }
}