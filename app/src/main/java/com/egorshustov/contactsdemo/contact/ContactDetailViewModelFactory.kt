package com.egorshustov.contactsdemo.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.egorshustov.contactsdemo.data.source.ContactsRepository

class ContactDetailViewModelFactory(
    private val contactsRepository: ContactsRepository,
    private val contactId: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactDetailViewModel(contactsRepository, contactId) as T
    }
}
