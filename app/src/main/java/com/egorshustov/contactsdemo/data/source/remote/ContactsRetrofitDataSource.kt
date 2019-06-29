package com.egorshustov.contactsdemo.data.source.remote

class ContactsRetrofitDataSource private constructor(
    private val contactsRetrofit: ContactsRetrofit
) : ContactsRemoteDataSource {
    override suspend fun getContacts(
        contactsUrlList: List<String>,
        loadContactsCallback: ContactsRemoteDataSource.LoadContactsCallback
    ) {
        contactsUrlList.forEach { contactsUrl ->
            try {
                val response = contactsRetrofit.getContacts(contactsUrl)
                if (response.isSuccessful) {
                    loadContactsCallback.onContactsLoaded(response.body())
                    loadContactsCallback.onServerResponseGot(
                        ResponseMessage.Success(response.message())
                    )
                } else {
                    loadContactsCallback.onServerResponseGot(
                        ResponseMessage.Error(
                            response.message(),
                            null
                        )
                    )
                }
            } catch (throwable: Throwable) {
                loadContactsCallback.onServerResponseGot(
                    ResponseMessage.Error(null, throwable)
                )
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