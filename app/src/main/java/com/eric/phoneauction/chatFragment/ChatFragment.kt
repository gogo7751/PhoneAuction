package com.eric.phoneauction.chatFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eric.phoneauction.NavigationDirections

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentChatBinding
import com.eric.phoneauction.detailChatFragment.DetailChatViewModel
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.util.Logger
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

        viewModel.liveChatRooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToChatToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(ChatFragmentDirections.actionChatFragmentToChatToDetailChatFragment(it))
            }
        })

        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
        return binding.root
    }



}
