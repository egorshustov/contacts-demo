package com.egorshustov.contactsdemo.contactlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ContactListViewModel(context: Application) : AndroidViewModel(context) {
    private val themesRepository = ThemesRepository(context)
    private val contactsRepository = ContactsRepository(context)

    private val liveUpdateContactsResponse = MutableLiveData<String>()

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getLiveUpdateContactsResponse(): LiveData<String> {
        return liveUpdateContactsResponse
    }

    fun setNextThemeId() {
        themesRepository.setNextThemeId()
    }

    fun getCurrentThemeID() = themesRepository.getCurrentThemeID()

    fun updateContacts(checkFetchTime: Boolean) {
        viewModelScope.launch {
            contactsRepository.updateContacts(checkFetchTime, liveUpdateContactsResponse)
        }
    }
}