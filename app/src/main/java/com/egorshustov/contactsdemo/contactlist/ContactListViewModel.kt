package com.egorshustov.contactsdemo.contactlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.egorshustov.contactsdemo.data.source.ThemesRepository

class ContactListViewModel(context: Application) : AndroidViewModel(context) {
    private val themesRepository = ThemesRepository(context)

    fun setNextThemeId() {
        themesRepository.setNextThemeId()
    }

    fun getCurrentThemeID() = themesRepository.getCurrentThemeID()
}