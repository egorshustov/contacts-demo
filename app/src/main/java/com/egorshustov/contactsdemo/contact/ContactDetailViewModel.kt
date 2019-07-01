package com.egorshustov.contactsdemo.contact

import androidx.lifecycle.ViewModel
import com.egorshustov.contactsdemo.data.source.ContactsRepository


class ContactDetailViewModel(
    contactsRepository: ContactsRepository,
    contactId: String
) : ViewModel() {
    val contact = contactsRepository.getLiveContact(contactId)
}