package com.egorshustov.contactsdemo.contactlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import com.egorshustov.contactsdemo.data.source.local.AppDatabase
import com.egorshustov.contactsdemo.utils.getLiveDataValue
import com.egorshustov.contactsdemo.utils.testContacts
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContactListViewModelTest {
    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: ContactListViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        appDatabase.contactDao().insertContacts(testContacts)

        val contactsRepository = ContactsRepository.getInstance(appDatabase.contactDao())
        val themesRepository = ThemesRepository(context)
        viewModel = ContactListViewModel(contactsRepository, themesRepository)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testMediatorLiveContactsAreNotNull() {
        ViewMatchers.assertThat(
            getLiveDataValue(viewModel.getMediatorLiveContacts())?.size,
            CoreMatchers.equalTo(testContacts.size)
        )
    }
}