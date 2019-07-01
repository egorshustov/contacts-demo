package com.egorshustov.contactsdemo.contactlist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.egorshustov.contactsdemo.ContactListActivity
import com.egorshustov.contactsdemo.databinding.FragmentContactListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_contact_list.*

class ContactListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var contactListViewModel: ContactListViewModel
    private lateinit var binding: FragmentContactListBinding
    private val contactListAdapter = ContactListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            layoutSwipeRefresh.setOnRefreshListener(this@ContactListFragment)
            recyclerContacts.adapter = contactListAdapter
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contactListViewModel = (activity as ContactListActivity).getContactListViewModel()
        observeMediatorLiveContacts()
        observeLiveUpdateContactsResponse()
    }

    override fun onResume() {
        super.onResume()
        (activity as ContactListActivity).edit_filter.visibility = View.VISIBLE
    }

    override fun onRefresh() {
        contactListViewModel.updateContacts(false)
    }

    private fun observeMediatorLiveContacts() {
        contactListViewModel.getMediatorLiveContacts().observe(viewLifecycleOwner, Observer { contactList ->
            binding.hasContacts = !contactList.isNullOrEmpty()
            contactListAdapter.submitList(contactList)
        })
    }

    private fun observeLiveUpdateContactsResponse() {
        contactListViewModel.getLiveUpdateContactsResponseMessage().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "observeLiveUpdateContactsResponse thread: ${Thread.currentThread().name}")
            binding.layoutSwipeRefresh.isRefreshing = false
            it.getErrorMessage()?.let { errorMessage ->
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        private const val TAG = "ContactListFragment"
    }
}
