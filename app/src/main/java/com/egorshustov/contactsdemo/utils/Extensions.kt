package com.egorshustov.contactsdemo.utils

/**
 * Various extension functions for AppCompatActivity.
 */

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.egorshustov.contactsdemo.ViewModelFactory

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)