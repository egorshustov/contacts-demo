package com.egorshustov.contactsdemo.contact

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.egorshustov.contactsdemo.data.Contact

class ContactViewModelFactory(
    private var application: Application,
    private var contact: Contact
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContactViewModel(
            application,
            contact
        ) as T
    }
}