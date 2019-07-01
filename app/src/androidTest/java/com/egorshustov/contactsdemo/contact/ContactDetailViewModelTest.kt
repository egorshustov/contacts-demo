package com.egorshustov.contactsdemo.contact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.egorshustov.contactsdemo.data.source.ContactsRepository
import com.egorshustov.contactsdemo.data.source.local.AppDatabase
import com.egorshustov.contactsdemo.utils.getLiveDataValue
import com.egorshustov.contactsdemo.utils.testContacts
import org.junit.*

class ContactDetailViewModelTest {
    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: ContactDetailViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        appDatabase.contactDao().insertContacts(testContacts)

        val contactsRepository = ContactsRepository.getInstance(appDatabase.contactDao())
        val contactId = "2"
        viewModel = ContactDetailViewModel(contactsRepository, contactId)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testContactIsNotNull() {
        Assert.assertNotNull(getLiveDataValue(viewModel.contact))
    }
}