package com.egorshustov.contactsdemo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.egorshustov.contactsdemo.contact.ContactViewModel
import com.egorshustov.contactsdemo.contactlist.ContactListViewModel
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import com.egorshustov.contactsdemo.utils.InjectorUtils

class ViewModelFactory private constructor(
    private val contactsRepository: ContactsRepository,
    private val themesRepository: ThemesRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = with(modelClass) {
        when {
            isAssignableFrom(ContactListViewModel::class.java) ->
                ContactListViewModel(contactsRepository, themesRepository)
            isAssignableFrom(ContactViewModel::class.java) ->
                ContactViewModel(themesRepository)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE
                ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE
                        ?: ViewModelFactory(
                            InjectorUtils.provideContactsRepository(application.applicationContext),
                            InjectorUtils.provideThemesRepository(application.applicationContext)
                        )
                            .also { INSTANCE = it }
                }
    }
}