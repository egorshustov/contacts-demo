package com.egorshustov.contactsdemo.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import com.egorshustov.contactsdemo.data.Contact
import com.egorshustov.contactsdemo.data.source.local.ContactsDao
import com.egorshustov.contactsdemo.data.source.remote.ContactJson
import com.egorshustov.contactsdemo.data.source.remote.ContactsRemoteDataSource
import com.egorshustov.contactsdemo.data.source.remote.ResponseMessage
import com.egorshustov.contactsdemo.utils.InjectorUtils
import com.egorshustov.contactsdemo.utils.TimeUtils
import com.egorshustov.contactsdemo.utils.TimeUtils.MILLISECONDS_IN_SECOND
import com.egorshustov.contactsdemo.utils.TimeUtils.unixMillisToDateString
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class ContactsRepository private constructor(
    private val contactDao: ContactsDao,
    private val contactsRemoteDataSource: ContactsRemoteDataSource
) {
    fun cacheIsOutdated(): Boolean {
        Log.d(TAG, "cacheIsOutdated thread: ${Thread.currentThread().name}")
        val oldestFetchTimeInUnixMillis = contactDao.getOldestFetchTime() ?: 0
        val currentTimeInUnixMillis = TimeUtils.getCurrentTimeInUnixMillis()

        if (currentTimeInUnixMillis - oldestFetchTimeInUnixMillis < MAX_INTERVAL_IN_SECONDS * MILLISECONDS_IN_SECOND) {
            return false
        }
        return true
    }

    suspend fun updateContacts(
        contactsUrlList: List<String>,
        publishUpdateContactsResponse: PublishSubject<ResponseMessage>
    ) {
        Log.d(TAG, "updateContacts thread: ${Thread.currentThread().name}")
        contactsRemoteDataSource.getContacts(contactsUrlList, object : ContactsRemoteDataSource.LoadContactsCallback {
            override fun onServerResponseGot(message: ResponseMessage) {
                Log.d(TAG, "publishUpdateContactsResponse: ${message.getErrorMessage() ?: "OK"}")
                publishUpdateContactsResponse.onNext(message)
            }

            override fun onContactsLoaded(contactJsonList: List<ContactJson>?) {
                Log.d(TAG, "onContactsLoaded")
                contactJsonList ?: return
                contactDao.insertContacts(mapJsonToContact(contactJsonList))
            }
        })
    }

    private fun mapJsonToContact(contactJsonList: List<ContactJson>): List<Contact> {
        val fetchTimeInUnixMillis = TimeUtils.getCurrentTimeInUnixMillis()
        return contactJsonList.map {
            Contact(
                it.id,
                it.name,
                it.phone,
                it.height,
                it.biography,
                it.temperament,
                unixMillisToDateString(it.educationPeriod?.start?.time) + " - " + unixMillisToDateString(it.educationPeriod?.end?.time),
                fetchTimeInUnixMillis
            )
        }
    }

    fun getLiveContact(contactId: String) = contactDao.getLiveContact(contactId)

    fun getLiveContacts(filter: String?): LiveData<List<Contact>?> {
        val filterText = when {
            (filter.isNullOrBlank() || filter == "") -> null
            else -> "%$filter%"
        }
        return contactDao.getLiveContacts(filterText)
    }

    companion object {
        private const val MAX_INTERVAL_IN_SECONDS = 60
        private const val TAG = "ContactsRepository"

        private var INSTANCE: ContactsRepository? = null

        @JvmStatic
        fun getInstance(contactDao: ContactsDao) =
            INSTANCE ?: synchronized(ContactsRepository::class.java) {
                INSTANCE ?: ContactsRepository(contactDao, InjectorUtils.bindContactsRemoteDataSource())
                    .also { INSTANCE = it }
            }
    }
}