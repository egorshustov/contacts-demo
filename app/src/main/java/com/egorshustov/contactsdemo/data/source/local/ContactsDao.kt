package com.egorshustov.contactsdemo.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.egorshustov.contactsdemo.data.Contact

@Dao
interface ContactsDao {
    @Query("select * from contacts order by name")
    fun getLiveContacts(): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContacts(contactList: List<Contact>)

    @Query("select fetch_time_unix_millis from contacts order by fetch_time_unix_millis limit 1")
    fun getOldestFetchTime(): Long?
}