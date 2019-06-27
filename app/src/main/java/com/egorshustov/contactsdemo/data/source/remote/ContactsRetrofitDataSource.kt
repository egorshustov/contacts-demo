package com.egorshustov.contactsdemo.data.source.remote

class ContactsRetrofitDataSource private constructor(
    private val contactsRetrofit: ContactsRetrofit
) : ContactsRemoteDataSource {
    override suspend fun getContacts(callback: ContactsRemoteDataSource.LoadContactsCallback) {
        ContactsRetrofit.contactsUrlList.forEach { contactsUrl ->
            try {
                val response = contactsRetrofit.getContacts(contactsUrl)
                if (response.isSuccessful) {
                    callback.onContactsLoaded(response.message(), response.body())
                } else {
                    callback.onDataNotAvailable(response.message())
                }
            } catch (t: Throwable) {
                callback.onDataNotAvailable(t.message.toString())
            }
        }
    }

    companion object {
        private var INSTANCE: ContactsRetrofitDataSource? = null

        @JvmStatic
        fun getInstance(contactsRetrofit: ContactsRetrofit): ContactsRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(ContactsRetrofitDataSource::javaClass) {
                    INSTANCE = ContactsRetrofitDataSource(contactsRetrofit)
                }
            }
            return INSTANCE!!
        }
    }
}