package com.eric.phoneauction.detailChatFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.eric.phoneauction.R
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


        binding.imageChatDetailSend.setOnClickListener{
            binding.editChatDetailInput.text.clear()
        }


        binding.imageChatDetailSend.setOnClickListener {
            viewModel.message.value?.let { it1 -> viewModel.document.value?.let { it2 ->
                viewModel.postMessage(it1,
                    it2
                )
            } }
        }

        return binding.root
    }

}
