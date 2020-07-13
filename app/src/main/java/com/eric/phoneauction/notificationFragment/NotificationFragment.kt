package com.eric.phoneauction.notificationFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNotificationBinding.inflate(inflater, container, false)







        return binding.root
    }



}
