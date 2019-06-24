package com.egorshustov.contactsdemo.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.egorshustov.contactsdemo.R
import com.egorshustov.contactsdemo.data.source.ThemesRepository
import com.egorshustov.contactsdemo.utils.ConstantsUtils.EXTRA_CONTACT
import com.egorshustov.contactsdemo.utils.TimeUtils.unixSecondsToDateString
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : AppCompatActivity() {
    private lateinit var contactViewModel: ContactViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getExtrasAndInitViewModel()
        setTheme(getCurrentTheme())
        setContentView(R.layout.activity_contact)
        setActionBar()
        initTextViews()
        setListeners()
    }

    private fun getExtrasAndInitViewModel() {
        contactViewModel = ViewModelProviders.of(
            this,
            ContactViewModelFactory(
                this.application,
                intent.getParcelableExtra(EXTRA_CONTACT)
            )
        ).get(ContactViewModel::class.java)
    }

    private fun getCurrentTheme(): Int {
        return when (contactViewModel.getCurrentThemeID()) {
            ThemesRepository.GREEN_THEME_ID -> {
                R.style.AppTheme_NoActionBar_Green
            }
            else -> {
                R.style.AppTheme_NoActionBar_Blue
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    private fun setActionBar() {
        setSupportActionBar(toolbar_contact)
        val icBack = ContextCompat.getDrawable(this, R.drawable.ic_back)
        supportActionBar?.setHomeAsUpIndicator(icBack)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initTextViews() {
        val contact = contactViewModel.contact
        text_name.text = contact.name
        text_phone.text = contact.phone
        text_temperament.text = contact.temperament
        val educationPeriodText = unixSecondsToDateString(contact.educationPeriod?.startUnixSeconds) +
                " - " + unixSecondsToDateString(contact.educationPeriod?.endUnixSeconds)
        text_education_period.text = educationPeriodText
        text_biography.text = contact.biography
    }

    private fun setListeners() {
        text_phone.setOnClickListener {
            this@ContactActivity.runOnUiThread {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${text_phone.text}")
                startActivity(callIntent)
            }
        }
    }
}
