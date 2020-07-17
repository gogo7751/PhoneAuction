package com.eric.phoneauction.detailChatFragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.eric.phoneauction.R
import com.eric.phoneauction.chatFragment.ChatViewModel
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.FragmentDetailChatBinding
import com.eric.phoneauction.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class DetailChatFragment : Fragment() {

    val viewModel: DetailChatViewModel by viewModels<DetailChatViewModel> { getVmFactory(DetailChatFragmentArgs.fromBundle(requireArguments()).event) }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailChatBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = DetailChatAdapter(viewModel)
        binding.recyclerviewChatDetail.adapter = adapter

        viewModel.liveMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.imageChatDetailBack.setOnClickListener {
            findNavController().navigateUp()
        }

        if (viewModel.event.value?.sellerName == UserManager.user.name) {
            binding.textChatTitle.text = UserManager.user.name
        } else {
            binding.textChatTitle.text = viewModel.event.value?.sellerName
        }

        binding.imageChatDetailSend.setOnClickListener {
            viewModel.message.value?.let { it1 -> viewModel.document.value?.let { it2 ->
                viewModel.postMessage(it1,
                    it2
                )
            } }
            Handler().postDelayed({binding.editChatDetailInput.text.clear()},500)
        }

        return binding.root
    }

}
