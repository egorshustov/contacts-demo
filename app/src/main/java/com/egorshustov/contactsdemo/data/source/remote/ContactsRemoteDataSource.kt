package com.egorshustov.contactsdemo.data.source.remote

import com.egorshustov.contactsdemo.data.Contact

interface ContactsRemoteDataSource {

    interface LoadContactsCallback {

        fun onServerResponseGot(message: ResponseMessage)

        fun onContactsLoaded(contactList: List<Contact>?)
    }

    suspend fun getContacts(contactsUrlList: List<String>, loadContactsCallback: LoadContactsCallback)
}