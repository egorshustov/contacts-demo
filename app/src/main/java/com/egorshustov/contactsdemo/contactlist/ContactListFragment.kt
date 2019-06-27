package com.egorshustov.contactsdemo.contactlist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.egorshustov.contactsdemo.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_contact_list.view.*

class ContactListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var fragmentView: View
    private lateinit var contactListViewModel: ContactListViewModel
    val contactListAdapter = ContactListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_contact_list, container, false)
        val recyclerContacts: RecyclerView = fragmentView.findViewById(R.id.recycler_contacts)
        recyclerContacts.layoutManager = LinearLayoutManager(activity)
        recyclerContacts.setHasFixedSize(true)

        recyclerContacts.adapter = contactListAdapter
        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentView.layout_swipe_refresh.setOnRefreshListener(this)
        val fragmentActivity = activity as ContactListActivity
        contactListViewModel = fragmentActivity.obtainViewModel()

        observeMediatorLiveContacts()
        observeLiveUpdateContactsResponse()
    }

    override fun onRefresh() {
        contactListViewModel.updateContacts(false)
    }

    private fun observeMediatorLiveContacts() {
        contactListViewModel.mediatorLiveContacts.observe(viewLifecycleOwner, Observer { contactList ->
            if (!contactList.isNullOrEmpty()) {
                fragmentView.progress_contacts.visibility = View.GONE
            }
            contactListAdapter.submitList(contactList)
        })
    }

    private fun observeLiveUpdateContactsResponse() {
        contactListViewModel.getLiveUpdateContactsResponse().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "observeLiveUpdateContactsResponse: ${Thread.currentThread().name}")
            Log.d(TAG, "updateContactsResponse: $it")
            fragmentView.layout_swipe_refresh.isRefreshing = false
            when {
                it == "OK" -> {
                }
                (it.contains("ConnectException") || it.contains("TimeoutException") || it.contains("UnknownHostException")) -> {
                    Snackbar.make(
                        fragmentView,
                        "Ошибка соединения: отсутствует подключение к сети или сервер недоступен",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                else -> {
                    Snackbar.make(fragmentView, "Ошибка $it", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    companion object {
        private const val TAG = "ContactListFragment"
        @JvmStatic
        fun newInstance() = ContactListFragment()
    }
}
