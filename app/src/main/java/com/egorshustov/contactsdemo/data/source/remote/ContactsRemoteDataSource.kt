package com.egorshustov.contactsdemo.data.source.remote

import com.egorshustov.contactsdemo.data.Contact

interface ContactsRemoteDataSource {

    interface LoadContactsCallback {

        fun onContactsLoaded(successMessage: String, contactList: List<Contact>?)

        fun onDataNotAvailable(errorMessage: String)
    }

    suspend fun getContacts(callback: LoadContactsCallback)
}