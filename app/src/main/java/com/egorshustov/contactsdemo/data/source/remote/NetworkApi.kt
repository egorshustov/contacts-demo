package com.egorshustov.contactsdemo.data.source.remote

import com.egorshustov.contactsdemo.data.Contact
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface NetworkApi {
    @GET
    fun getContacts(@Url url: String): Call<List<Contact>?>

    companion object Factory {
        val contactsUrlList = listOf(
            "https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/generated-01.json",
            "https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/generated-02.json",
            "https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/generated-03.json"
        )

        fun create(): NetworkApi {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NetworkApi::class.java)
        }
    }
}