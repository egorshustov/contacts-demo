package com.egorshustov.contactsdemo.data.source

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.egorshustov.contactsdemo.data.Contact
import com.egorshustov.contactsdemo.data.source.local.AppDatabase
import com.egorshustov.contactsdemo.data.source.local.ContactsDao
import com.egorshustov.contactsdemo.data.source.remote.NetworkApi
import com.egorshustov.contactsdemo.utils.TimeUtils
import com.egorshustov.contactsdemo.utils.TimeUtils.MILLISECONDS_IN_SECOND
import retrofit2.Call
import retrofit2.Response

class ContactsRepository(application: Application) {
    private val contactDao: ContactsDao
    private val networkApi: NetworkApi

    init {
        val database = AppDatabase.getInstance(application)
        contactDao = database.contactDao()
        networkApi = NetworkApi.create()
    }

    fun updateContacts(checkFetchTime: Boolean, liveResponseMessage: MutableLiveData<String>) {
        if (checkFetchTime) {
            val oldestFetchTimeInUnixMillis = contactDao.getOldestFetchTime() ?: 0
            val currentTimeInUnixMillis = TimeUtils.getCurrentTimeInUnixMillis()

            if (currentTimeInUnixMillis - oldestFetchTimeInUnixMillis < MAX_INTERVAL_IN_SECONDS * MILLISECONDS_IN_SECOND) {
                return
            }
        }

        NetworkApi.contactsUrlList.forEach {
            networkApi.getContacts(it).enqueue(object : retrofit2.Callback<List<Contact>?> {
                override fun onFailure(call: Call<List<Contact>?>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    liveResponseMessage.value = t.toString()
                }

                override fun onResponse(call: Call<List<Contact>?>, response: Response<List<Contact>?>) {
                    liveResponseMessage.value = response.message()
                    if (!response.isSuccessful) {
                        return
                    }
                    val fetchTimeInUnixMillis = TimeUtils.getCurrentTimeInUnixMillis()
                    response.body()?.forEach { contact ->
                        contact.fetchTimeUnixMillis = fetchTimeInUnixMillis
                    }
                }
            })
        }
    }

    private companion object {
        const val MAX_INTERVAL_IN_SECONDS = 60
        const val TAG = "ContactsRepository"
    }
}