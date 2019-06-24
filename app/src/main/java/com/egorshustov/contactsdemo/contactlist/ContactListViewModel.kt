package com.egorshustov.contactsdemo.contactlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.egorshustov.contactsdemo.data.Contact
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ContactListViewModel(context: Application) : AndroidViewModel(context) {
    private val contactsRepository = ContactsRepository(context)
    private val themesRepository = ThemesRepository(context)

    var mediatorLiveContacts = MediatorLiveData<List<Contact>>()
    var liveContacts: LiveData<List<Contact>?>? = null
    private val liveUpdateContactsResponse = MutableLiveData<String>()

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        filterContacts(null)
    }

    fun filterContacts(filter: String?) {
        liveContacts?.let { mediatorLiveContacts.removeSource(it) }
        liveContacts = contactsRepository.getLiveContacts(filter)
        liveContacts?.let {
            mediatorLiveContacts.addSource(it) { list ->
                mediatorLiveContacts.value = list
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getLiveUpdateContactsResponse(): LiveData<String> {
        return liveUpdateContactsResponse
    }

    fun updateContacts(checkFetchTime: Boolean) {
        viewModelScope.launch {
            contactsRepository.updateContacts(checkFetchTime, liveUpdateContactsResponse)
        }
    }

    fun setNextThemeId() {
        themesRepository.setNextThemeId()
    }

    fun getCurrentThemeID() = themesRepository.getCurrentThemeID()
}