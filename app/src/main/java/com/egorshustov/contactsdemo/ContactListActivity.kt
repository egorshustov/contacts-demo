package com.egorshustov.contactsdemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.egorshustov.contactsdemo.contactlist.ContactListViewModel
import com.egorshustov.contactsdemo.databinding.ActivityContactListBinding
import com.egorshustov.contactsdemo.utils.ThemesUtils
import com.egorshustov.contactsdemo.utils.obtainContactListViewModel

class ContactListActivity : AppCompatActivity() {
    private lateinit var contactListViewModel: ContactListViewModel
    private lateinit var binding: ActivityContactListBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactListViewModel = getContactListViewModel()
        setTheme(getCurrentTheme())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_list)

        drawerLayout = binding.drawerContactList

        navController = findNavController(R.id.contacts_nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        setSupportActionBar(binding.toolbarContactList)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navContactList.setupWithNavController(navController)
        setListeners()
    }

    private fun getCurrentTheme(): Int {
        return when (contactListViewModel.getCurrentThemeID()) {
            ThemesUtils.GREEN_THEME_ID -> {
                R.style.AppTheme_NoActionBar_Green
            }
            else -> {
                R.style.AppTheme_NoActionBar_Blue
            }
        }
    }

    fun getContactListViewModel(): ContactListViewModel = obtainContactListViewModel()

    private fun setListeners() {
        binding.navContactList.menu.findItem(R.id.nav_theme_change).setOnMenuItemClickListener {
            contactListViewModel.setNextThemeId()
            recreate()
            true
        }
        binding.editFilter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                contactListViewModel.filterContacts(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerContactList.isDrawerOpen(GravityCompat.START)) {
            binding.drawerContactList.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}
