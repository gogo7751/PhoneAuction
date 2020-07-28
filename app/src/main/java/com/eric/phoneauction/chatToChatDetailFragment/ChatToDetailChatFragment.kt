package com.eric.phoneauction.chatToChatDetailFragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.NavigationDirections
import com.eric.phoneauction.chatFragment.ChatViewModel
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.FragmentChatToDetailChatBinding
import com.eric.phoneauction.databinding.FragmentDetailChatBinding
import com.eric.phoneauction.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple [Fragment] subclass.
 */
class ChatToDetailChatFragment : Fragment() {

    val viewModel: ChatToDetailChatViewModel by viewModels<ChatToDetailChatViewModel> { getVmFactory(ChatToDetailChatFragmentArgs.fromBundle(requireArguments()).chatRoom) }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatToDetailChatBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = ChatToDetailChatAdapter(viewModel)
//        adapter.setHasStableIds(true)
        binding.recyclerviewChatToDetail.adapter = adapter

        viewModel.liveMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.imageChatToDetailBack.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalChatFragment())
        }

        if (viewModel.chatRoom.value?.senderId == UserManager.userId) {
            binding.textChatToDetailTitle.text = viewModel.chatRoom.value?.receiverName
        } else {
            binding.textChatToDetailTitle.text = viewModel.chatRoom.value?.senderName
        }

        binding.imageChatToDetailSend.setOnClickListener {
            viewModel.message.value?.let { it1 -> viewModel.document.value?.let { it2 ->
                viewModel.postMessage(it1,
                    it2
                )
            } }
            Handler().postDelayed({binding.editChatToDetailInput.text.clear()},500)
        }

        (activity as AppCompatActivity).bottomNavView.visibility = View.GONE

        return binding.root
    }

    //bottom navigation view gone
    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
    }

}
