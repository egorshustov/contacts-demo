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
    @ColumnInfo(name = "id")
    val contactId: String,
    val name: String?,
    val phone: String?,
    val height: Float?,
    val biography: String?,
    val temperament: String?,
    val educationPeriod: String?,
    @ColumnInfo(name = "fetch_time_unix_millis")
    var fetchTimeUnixMillis: Long?
) : Parcelable {
    fun contentsEquals(otherContact: Contact): Boolean {
        return name == otherContact.name &&
                phone == otherContact.phone &&
                height == otherContact.height
    }
}