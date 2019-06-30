package com.egorshustov.contactsdemo.data.source.remote

data class ContactJson(
    val id: String,
    val name: String?,
    val phone: String?,
    val height: Float?,
    val biography: String?,
    val temperament: String?,
    val educationPeriod: EducationPeriodJson?
)