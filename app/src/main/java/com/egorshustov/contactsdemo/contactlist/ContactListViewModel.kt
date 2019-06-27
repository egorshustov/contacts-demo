package com.egorshustov.contactsdemo.contactlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import com.egorshustov.contactsdemo.data.Contact
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ContactListViewModel(context: Application) : AndroidViewModel(context) {
    private val contactsRepository = ContactsRepository(context)
    private val themesRepository = ThemesRepository(context)

    var mediatorLiveContacts = MediatorLiveData<List<Contact>>()
    var liveContacts: LiveData<List<Contact>?>? = null
    private val publishUpdateContactsResponse = PublishSubject.create<String>()
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        updateContacts(true)
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
        return LiveDataReactiveStreams.fromPublisher(
            publishUpdateContactsResponse
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .throttleFirst(3, TimeUnit.SECONDS)
        )
    }

    fun updateContacts(checkFetchTime: Boolean) {
        viewModelScope.launch {
            contactsRepository.updateContacts(checkFetchTime, publishUpdateContactsResponse)
        }
    }

    fun setNextThemeId() {
        themesRepository.setNextThemeId()
    }

    fun getCurrentThemeID() = themesRepository.getCurrentThemeID()
}