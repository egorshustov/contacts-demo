package com.egorshustov.contactsdemo.data.source.remote

interface ContactsRemoteDataSource {

    interface LoadContactsCallback {

        fun onServerResponseGot(message: ResponseMessage)

        fun onContactsLoaded(contactJsonList: List<ContactJson>?)
    }

    suspend fun getContacts(contactsUrlList: List<String>, loadContactsCallback: LoadContactsCallback)
}