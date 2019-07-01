package com.egorshustov.contactsdemo.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.egorshustov.contactsdemo.contactlist.ContactListViewModel
import com.egorshustov.contactsdemo.contactlist.ContactListViewModelFactory

fun AppCompatActivity.obtainContactListViewModel() =
    ViewModelProviders.of(
        this,
        ContactListViewModelFactory.getInstance(application)
    ).get(ContactListViewModel::class.java)