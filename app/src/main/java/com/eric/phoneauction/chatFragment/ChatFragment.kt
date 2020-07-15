package com.eric.phoneauction.chatFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentChatBinding
import com.eric.phoneauction.ext.getVmFactory

class ChatFragment : Fragment() {

    private val viewModel by viewModels<ChatViewModel> { getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        return binding.root
    }



}
