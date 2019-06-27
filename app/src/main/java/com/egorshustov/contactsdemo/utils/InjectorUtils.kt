package com.egorshustov.contactsdemo.utils

import android.content.Context
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import com.egorshustov.contactsdemo.data.source.local.AppDatabase
import com.egorshustov.contactsdemo.data.source.remote.NetworkApi

object InjectorUtils {
    fun provideContactsRepository(context: Context): ContactsRepository {
        val database = AppDatabase.getInstance(context)
        return ContactsRepository.getInstance(database.contactDao(), NetworkApi.create())
    }

    fun provideThemesRepository(context: Context): ThemesRepository {
        return ThemesRepository(context)
    }
}