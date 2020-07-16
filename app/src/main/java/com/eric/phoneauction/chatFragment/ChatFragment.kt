package com.eric.phoneauction.chatFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentChatBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.util.Logger

class ChatFragment : Fragment() {

    private val viewModel by viewModels<ChatViewModel> { getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ChatAdapter(viewModel)
        binding.recyclerviewChat.adapter = adapter

        viewModel.liveChatRooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })



        return binding.root
    }



}
