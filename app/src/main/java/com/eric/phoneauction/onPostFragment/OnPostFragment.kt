package com.eric.phoneauction.onPostFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentOnAuctionBinding
import com.eric.phoneauction.databinding.FragmentOnPostBinding
import com.eric.phoneauction.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class OnPostFragment : Fragment() {

    val viewModel: OnPostViewModel by viewModels<OnPostViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOnPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = OnPostAdapter()
        binding.recyclerviewOnPost.adapter = adapter

        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.imageOnPostBack.setOnClickListener {
            findNavController().navigateUp()
        }


        return binding.root
    }

}
