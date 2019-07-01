package com.egorshustov.contactsdemo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.egorshustov.contactsdemo.data.source.local.AppDatabase
import com.egorshustov.contactsdemo.data.source.local.ContactsDao
import com.egorshustov.contactsdemo.utils.getLiveDataValue
import com.egorshustov.contactsdemo.utils.testContacts
import io.reactivex.internal.util.NotificationLite.getValue
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContactsDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var contactsDao: ContactsDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        contactsDao = database.contactDao()

        database.contactDao().insertContacts(testContacts)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetOldestFetchTime() {
        assertThat(
            getValue(contactsDao.getOldestFetchTime()),
            equalTo(0L)
        )
    }

    @Test
    fun testGetLiveContact() {
        assertThat(
            getLiveDataValue(contactsDao.getLiveContact(testContacts[1].contactId)),
            equalTo(testContacts[1])
        )
    }

    @Test
    fun testGetAllLiveContacts() {
        assertThat(
            getLiveDataValue(contactsDao.getLiveContacts(null))?.size,
            equalTo(testContacts.size)
        )
    }
}