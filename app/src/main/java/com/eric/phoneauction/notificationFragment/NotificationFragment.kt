package com.eric.phoneauction.notificationFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentNotificationBinding
import com.eric.phoneauction.ext.getVmFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class NotificationFragment : Fragment() {

    val viewModel: NotificationViewModel by viewModels { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNotificationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = NotificationAdapter(viewModel)
        adapter.setHasStableIds(true)
        binding.recyclerviewNotification.adapter = adapter

        viewModel.liveNotifications.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if (it.isEmpty()) {
                binding.textNoContent.visibility = View.VISIBLE
            } else {
                binding.textNoContent.visibility = View.GONE
            }
    })



        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavView).visibility = View.VISIBLE
        return binding.root
    }



}
