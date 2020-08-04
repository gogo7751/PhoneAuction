package com.eric.phoneauction.chat


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.eric.phoneauction.databinding.FragmentChatBinding
import com.eric.phoneauction.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*

class ChatFragment : Fragment() {

    private val viewModel by viewModels<ChatViewModel> { getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ChatAdapter(ChatAdapter.OnClickListener{
            viewModel.navigateToChatToDetail(it)
        },viewModel)
        adapter.setHasStableIds(true)
        binding.recyclerviewChat.adapter = adapter

        viewModel.liveChatRooms.observe(viewLifecycleOwner, Observer { chatRoom ->
            adapter.submitList(chatRoom)
        })

        viewModel.navigateToChatToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(ChatFragmentDirections.actionChatFragmentToChatToDetailChatFragment(it))
            }
        })

        viewModel.isEmpty.observe(viewLifecycleOwner, Observer {
            binding.textNoContent.visibility = View.GONE
        })

        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
        return binding.root
    }



}
