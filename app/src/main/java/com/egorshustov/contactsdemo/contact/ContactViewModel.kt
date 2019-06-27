package com.egorshustov.contactsdemo.contact

import androidx.lifecycle.ViewModel
import com.egorshustov.contactsdemo.data.source.ThemesRepository


class ContactViewModel(private val themesRepository: ThemesRepository) : ViewModel() {
    fun getCurrentThemeID() = themesRepository.getCurrentThemeID()
}