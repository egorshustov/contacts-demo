package com.egorshustov.contactsdemo.contact

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.egorshustov.contactsdemo.data.Contact
import com.egorshustov.contactsdemo.data.source.ThemesRepository


class ContactViewModel(context: Application, var contact: Contact) : AndroidViewModel(context) {
    private val themesRepository = ThemesRepository(context)

    fun getCurrentThemeID() = themesRepository.getCurrentThemeID()
}