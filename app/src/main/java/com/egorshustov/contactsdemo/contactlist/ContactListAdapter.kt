package com.egorshustov.contactsdemo.contactlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.contactsdemo.R
import com.egorshustov.contactsdemo.data.Contact
import com.egorshustov.contactsdemo.databinding.ItemContactBinding

class ContactListAdapter : ListAdapter<Contact, ContactListAdapter.ContactHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        return ContactHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_contact, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        getItem(position).let { contact ->
            with(holder) {
                itemView.tag = contact
                bind(createOnClickListener(contact.contactId), contact)
            }
        }
    }

    private fun createOnClickListener(contactId: String): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                ContactListFragmentDirections.actionContactListFragmentToContactFragment(contactId)
            it.findNavController().navigate(direction)
        }
    }

    inner class ContactHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, contact: Contact) {
            with(binding) {
                clickListener = listener
                viewModel = ContactItemViewModel(contact)
                executePendingBindings()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.contactId == newItem.contactId
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.contentsEquals(newItem)
            }
        }
    }
}