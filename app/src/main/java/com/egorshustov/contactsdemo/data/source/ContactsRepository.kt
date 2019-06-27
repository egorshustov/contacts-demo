package com.egorshustov.contactsdemo.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import com.egorshustov.contactsdemo.data.Contact
import com.egorshustov.contactsdemo.data.source.local.ContactsDao
import com.egorshustov.contactsdemo.data.source.remote.NetworkApi
import com.egorshustov.contactsdemo.utils.TimeUtils
import com.egorshustov.contactsdemo.utils.TimeUtils.MILLISECONDS_IN_SECOND
import com.egorshustov.contactsdemo.utils.TimeUtils.timeStringToUnixSeconds
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.delay
import retrofit2.Response

class ContactsRepository(
    private val contactDao: ContactsDao,
    private val networkApi: NetworkApi
) {

    suspend fun updateContacts(checkFetchTime: Boolean, publishResponseMessage: PublishSubject<String>) {
        Log.d(TAG, "updateContacts: ${Thread.currentThread().name}")
        if (checkFetchTime) {
            val oldestFetchTimeInUnixMillis = contactDao.getOldestFetchTime() ?: 0
            val currentTimeInUnixMillis = TimeUtils.getCurrentTimeInUnixMillis()

            if (currentTimeInUnixMillis - oldestFetchTimeInUnixMillis < MAX_INTERVAL_IN_SECONDS * MILLISECONDS_IN_SECOND) {
                Log.d(TAG, "Do not update contacts")
                return
            }
            delay(500)
        }

        NetworkApi.contactsUrlList.forEach lit@{ contactsUrl ->
            var response: Response<List<Contact>?>? = null
            try {
                Log.d(TAG, "Update contacts from $contactsUrl")
                response = networkApi.getContacts(contactsUrl)
            } catch (t: Throwable) {
                Log.d(TAG, "publishResponseMessage: ${t.message.toString()}")
                publishResponseMessage.onNext(t.message.toString())
            }

            response ?: return@lit
            Log.d(TAG, "publishResponseMessage: ${response.message()}")
            publishResponseMessage.onNext(response.message())
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

    companion object {
        private const val MAX_INTERVAL_IN_SECONDS = 60
        private const val TAG = "ContactsRepository"

        private var INSTANCE: ContactsRepository? = null

        @JvmStatic
        fun getInstance(contactDao: ContactsDao, networkApi: NetworkApi) =
            INSTANCE ?: synchronized(ContactsRepository::class.java) {
                INSTANCE ?: ContactsRepository(contactDao, networkApi)
                    .also { INSTANCE = it }
            }
    }
}