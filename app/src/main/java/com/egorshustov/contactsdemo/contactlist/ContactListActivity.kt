package com.egorshustov.contactsdemo.contactlist

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.egorshustov.contactsdemo.R
import com.egorshustov.contactsdemo.data.source.ThemesRepository.Companion.GREEN_THEME_ID
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.android.synthetic.main.app_bar_contact_list.*

class ContactListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var contactListViewModel: ContactListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel::class.java)
        setTheme(getCurrentTheme())
        setContentView(R.layout.activity_contact_list)
        setSupportActionBar(toolbar_contact_list)

        setDrawerListeners()
    }

    override fun onResume() {
        super.onResume()
        nav_contact_list.menu.findItem(R.id.nav_contact_list).isChecked = true
    }

    private fun getCurrentTheme(): Int {
        return when (contactListViewModel.getCurrentThemeID()) {
            GREEN_THEME_ID -> {
                R.style.AppTheme_NoActionBar_Green
            }
            else -> {
                R.style.AppTheme_NoActionBar_Blue
            }
        }
    }

    private fun setDrawerListeners() {
        val toggle = ActionBarDrawerToggle(
            this, drawer_contact_list, toolbar_contact_list,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_contact_list.addDrawerListener(toggle)
        toggle.syncState()
        nav_contact_list.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_contact_list.isDrawerOpen(GravityCompat.START)) {
            drawer_contact_list.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_contact_list -> {
                drawer_contact_list.closeDrawer(GravityCompat.START)
            }
            R.id.nav_about_developer -> {

                drawer_contact_list.closeDrawer(GravityCompat.START)
            }
            R.id.nav_theme_change -> {
                contactListViewModel.setNextThemeId()
                recreate()
            }
        }
        return true
    }
}
