package com.egorshustov.contactsdemo.contactlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.egorshustov.contactsdemo.utils.testContacts
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContactItemViewModelTest {
    private lateinit var viewModel: ContactItemViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = ContactItemViewModel(testContacts[0])
    }

    @Test
    fun testContactNameIsNotNull() {
        Assert.assertNotNull(viewModel.name)
    }

    @Test
    fun testContactPhoneIsNotNull() {
        Assert.assertNotNull(viewModel.phone)
    }

    @Test
    fun testContactHeightIsNotNull() {
        Assert.assertNotNull(viewModel.height)
    }
}