package com.egorshustov.contactsdemo.contactlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.contactsdemo.R
import com.egorshustov.contactsdemo.data.Contact

class ContactListAdapter : ListAdapter<Contact, ContactListAdapter.ContactHolder>(DIFF_CALLBACK) {
    private var onContactClickListener: OnContactClickListener? = null

    interface OnContactClickListener {
        fun onContactClick(contact: Contact)
    }

    fun setOnContactClickListener(listener: OnContactClickListener) {
        this.onContactClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val currentContact = getItem(position)
        holder.textContactName.text = currentContact.name
        holder.textContactPhone.text = currentContact.phone
        holder.textContactHeight.text = currentContact.height.toString()
    }

    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textContactName: TextView = itemView.findViewById(R.id.text_contact_name)
        val textContactPhone: TextView = itemView.findViewById(R.id.text_contact_phone)
        val textContactHeight: TextView = itemView.findViewById(R.id.text_contact_height)

        init {
            itemView.setOnClickListener {
                val contact = getItem(adapterPosition)
                onContactClickListener?.onContactClick(contact)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.contentsEquals(newItem)
            }
        }
    }
}