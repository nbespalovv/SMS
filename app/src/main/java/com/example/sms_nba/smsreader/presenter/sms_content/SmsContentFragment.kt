package com.example.sms_nba.smsreader.presenter.sms_content

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sms_nba.R
import com.example.sms_nba.databinding.FragmentSmsContentBinding
import com.example.sms_nba.smsreader.presenter.SmsContentAdapter

class SmsContentFragment : Fragment(R.layout.fragment_sms_content) {

    private val binding: FragmentSmsContentBinding by viewBinding()
    private val viewModel: SmsContentViewModel by viewModels()
    private val adapter = SmsContentAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: SmsContentFragmentArgs by navArgs()
        val addressId = args.addressId

        binding.nameAddress.text = addressId

        binding.smsContentRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.smsContentRecycler.adapter = adapter

        viewModel.chatEntries.observe(viewLifecycleOwner) { chatEntries ->
            val selectedEntry = chatEntries.find { it.adress == addressId }
            selectedEntry?.let {
                adapter.submitList(it.messages)
            }
        }

        viewModel.loadSmsMessages(requireContext().contentResolver, addressId)
    }
}