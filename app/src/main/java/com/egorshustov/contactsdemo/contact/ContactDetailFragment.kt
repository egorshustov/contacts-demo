package com.egorshustov.contactsdemo.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.egorshustov.contactsdemo.ContactListActivity
import com.egorshustov.contactsdemo.R
import com.egorshustov.contactsdemo.databinding.FragmentContactDetailBinding
import com.egorshustov.contactsdemo.utils.InjectorUtils
import kotlinx.android.synthetic.main.activity_contact_list.*

class ContactDetailFragment : Fragment() {

    private val args: ContactDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentContactDetailBinding

    private val contactDetailViewModel: ContactDetailViewModel by viewModels {
        InjectorUtils.provideContactDetailViewModelFactory(requireActivity(), args.contactId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentContactDetailBinding>(
            inflater, R.layout.fragment_contact_detail, container, false
        ).apply {
            viewModel = contactDetailViewModel
            lifecycleOwner = this@ContactDetailFragment
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        (activity as ContactListActivity).search_contact_list.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun setListeners() {
        binding.textPhone.setOnClickListener {
            activity?.runOnUiThread {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${binding.textPhone.text}")
                startActivity(callIntent)
            }
        }
    }
}
