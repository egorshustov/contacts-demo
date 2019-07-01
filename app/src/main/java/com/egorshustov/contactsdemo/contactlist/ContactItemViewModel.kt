package com.egorshustov.contactsdemo.contactlist

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.egorshustov.contactsdemo.data.Contact


class ContactItemViewModel(contact: Contact) : ViewModel() {
    val name = ObservableField<String>(contact.name)
    val phone = ObservableField<String>(contact.phone)
    val height = ObservableField(contact.height.toString())
}