package com.egorshustov.contactsdemo.contactlist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import com.egorshustov.contactsdemo.utils.InjectorUtils


class ContactListViewModelFactory(
    private val contactsRepository: ContactsRepository,
    private val themesRepository: ThemesRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactListViewModel(contactsRepository, themesRepository) as T
    }

    companion object {
        @Volatile
        private var INSTANCE: ContactListViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE
                ?: synchronized(ContactListViewModelFactory::class.java) {
                    INSTANCE
                        ?: InjectorUtils.provideContactListViewModelFactory(application.applicationContext)
                            .also { INSTANCE = it }
                }
    }
}