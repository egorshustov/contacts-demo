package com.egorshustov.contactsdemo.contactlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.egorshustov.contactsdemo.data.Contact
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import com.egorshustov.contactsdemo.data.source.remote.ContactsRetrofit
import com.egorshustov.contactsdemo.data.source.remote.ResponseMessage
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class ContactListViewModel(
    private val contactsRepository: ContactsRepository,
    private val themesRepository: ThemesRepository
) : ViewModel() {
    private val mediatorLiveContacts = MediatorLiveData<List<Contact>>()
    private var liveContacts: LiveData<List<Contact>?>? = null
    private val publishUpdateContactsResponse = PublishSubject.create<ResponseMessage>()
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        updateContacts(true)
        filterContacts(null)
    }

    fun filterContacts(filter: String?) {
        liveContacts?.let {
            mediatorLiveContacts.removeSource(it)
        }
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

    fun getLiveUpdateContactsResponseMessage(): LiveData<ResponseMessage> {
        return LiveDataReactiveStreams.fromPublisher(
            publishUpdateContactsResponse
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .throttleFirst(1, TimeUnit.SECONDS)
        )
    }

    fun getMediatorLiveContacts(): LiveData<List<Contact>?> {
        return mediatorLiveContacts
    }

    fun updateContacts(checkIfCacheIsOutdated: Boolean) {
        viewModelScope.launch {
            val needToUpdateContacts = if (checkIfCacheIsOutdated) {
                delay(500)
                contactsRepository.cacheIsOutdated()
            } else {
                true
            }

            if (needToUpdateContacts) {
                contactsRepository.updateContacts(ContactsRetrofit.contactsUrlList, publishUpdateContactsResponse)
            }
        }
    }

    fun setNextThemeId() {
        themesRepository.setNextThemeId()
    }

    fun getCurrentThemeID() = themesRepository.getCurrentThemeID()
}