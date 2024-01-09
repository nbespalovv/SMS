package com.example.sms_nba.smsreader.presenter.sms_list

import android.Manifest.permission.READ_SMS
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sms_nba.R
import com.example.sms_nba.databinding.FragmentSmsListBinding
import com.example.sms_nba.smsreader.data.SmsChatEntry
import com.example.sms_nba.smsreader.presenter.SmsEntriesAdapter
import com.example.sms_nba.smsreader.requirePermission

class SmsListFragment: Fragment(R.layout.fragment_sms_list) {

    private val binding: FragmentSmsListBinding by viewBinding()
    private val viewModel: SmsListViewModel by viewModels()

    private val navController by lazy { findNavController() }

    private val adapter = SmsEntriesAdapter(
        ::onChatItemClick
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chatMessageEntries.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        initializeRecycler()
        requireSmsPermission()
    }

    private fun requireSmsPermission() {
        requirePermission(
            permission = READ_SMS,
            successDelegate = {
                viewModel.loadSmsMessages(requireContext().contentResolver)
            },
            failureDelegate = {

            }
        )
    }


    private fun onChatItemClick(entry: SmsChatEntry) {
        //Toast.makeText(requireContext(), entry.adress, Toast.LENGTH_SHORT).show()
        //navController.navigate(R.id.smsContentFragment)
        navController.navigate(SmsListFragmentDirections.actionSmsListFragmentToSmsContentFragment(addressId = entry.adress))
    }

    private fun initializeRecycler() = with(binding.smsListRecycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = this@SmsListFragment.adapter
        addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }
}