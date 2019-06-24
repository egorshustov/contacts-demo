package com.egorshustov.contactsdemo.data.source

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egorshustov.contactsdemo.data.Contact
import com.egorshustov.contactsdemo.data.source.local.AppDatabase
import com.egorshustov.contactsdemo.data.source.local.ContactsDao
import com.egorshustov.contactsdemo.data.source.remote.NetworkApi
import com.egorshustov.contactsdemo.utils.TimeUtils
import com.egorshustov.contactsdemo.utils.TimeUtils.MILLISECONDS_IN_SECOND
import com.egorshustov.contactsdemo.utils.TimeUtils.timeStringToUnixSeconds
import retrofit2.Response

class ContactsRepository(application: Application) {
    private val contactDao: ContactsDao
    private val networkApi: NetworkApi

    init {
        val database = AppDatabase.getInstance(application)
        contactDao = database.contactDao()
        networkApi = NetworkApi.create()
    }

    suspend fun updateContacts(checkFetchTime: Boolean, liveResponseMessage: MutableLiveData<String>) {
        Log.d(TAG, "updateContacts: ${Thread.currentThread().name}")
        if (checkFetchTime) {
            val oldestFetchTimeInUnixMillis = contactDao.getOldestFetchTime() ?: 0
            val currentTimeInUnixMillis = TimeUtils.getCurrentTimeInUnixMillis()

            if (currentTimeInUnixMillis - oldestFetchTimeInUnixMillis < MAX_INTERVAL_IN_SECONDS * MILLISECONDS_IN_SECOND) {
                return
            }
        }

        NetworkApi.contactsUrlList.forEach lit@{ contactsUrl ->
            var response: Response<List<Contact>?>? = null
            try {
                response = networkApi.getContacts(contactsUrl)
            } catch (e: Exception) {
                liveResponseMessage.postValue(e.toString())
            }

            response ?: return@lit
            liveResponseMessage.postValue(response.message())
            val contactList: List<Contact>? = response.body()
            if (!response.isSuccessful || contactList == null) {
                return@lit
            }
            contactDao.insertContacts(fillContactListData(contactList))
        }
    }

    private fun fillContactListData(contactList: List<Contact>): List<Contact> {
        val fetchTimeInUnixMillis = TimeUtils.getCurrentTimeInUnixMillis()
        val timePattern = "yyyy-MM-dd'T'HH:mm:ssZ"

        contactList.forEach { contact ->
            contact.fetchTimeUnixMillis = fetchTimeInUnixMillis
            contact.educationPeriod?.startUnixSeconds =
                timeStringToUnixSeconds(timePattern, contact.educationPeriod?.startDateTimeString)
            contact.educationPeriod?.endUnixSeconds =
                timeStringToUnixSeconds(timePattern, contact.educationPeriod?.endDateTimeString)
        }
        return contactList
    }

    fun getLiveContacts(filter: String?): LiveData<List<Contact>?> {
        val filterText = when {
            (filter.isNullOrBlank() || filter == "") -> null
            else -> "%$filter%"
        }
        return contactDao.getLiveContacts(filterText)
    }

    private companion object {
        const val MAX_INTERVAL_IN_SECONDS = 60
        const val TAG = "ContactsRepository"
    }
}