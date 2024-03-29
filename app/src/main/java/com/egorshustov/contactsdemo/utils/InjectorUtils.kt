package com.egorshustov.contactsdemo.utils

import android.content.Context
import com.egorshustov.contactsdemo.contact.ContactDetailViewModelFactory
import com.egorshustov.contactsdemo.contactlist.ContactListViewModelFactory
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import com.egorshustov.contactsdemo.data.source.local.AppDatabase
import com.egorshustov.contactsdemo.data.source.remote.ContactsRemoteDataSource
import com.egorshustov.contactsdemo.data.source.remote.ContactsRetrofit
import com.egorshustov.contactsdemo.data.source.remote.ContactsRetrofitDataSource

object InjectorUtils {
    fun bindContactsRemoteDataSource(): ContactsRemoteDataSource {
        val contactsRetrofit = ContactsRetrofit.create()
        return ContactsRetrofitDataSource.getInstance(contactsRetrofit)
    }

    fun provideContactsRepository(context: Context): ContactsRepository {
        val database = AppDatabase.getInstance(context)
        return ContactsRepository.getInstance(database.contactDao())
    }

    fun provideThemesRepository(context: Context): ThemesRepository {
        return ThemesRepository(context)
    }

    fun provideContactDetailViewModelFactory(
        context: Context,
        contactId: String
    ): ContactDetailViewModelFactory {
        return ContactDetailViewModelFactory(
            provideContactsRepository(context),
            contactId
        )
    }

    fun provideContactListViewModelFactory(
        context: Context
    ): ContactListViewModelFactory {
        return ContactListViewModelFactory(
            provideContactsRepository(context),
            provideThemesRepository(context)
        )
    }
}