package com.eric.phoneauction.onAuctionFragment

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
import com.eric.phoneauction.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class OnAuctionFragment : Fragment() {

    val viewModel: OnAuctionViewModel by viewModels<OnAuctionViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOnAuctionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = OnAuctionAdapter()
        adapter.setHasStableIds(true)
        binding.recyclerviewOnAuction.adapter = adapter

        viewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.imageOnAuctionBack.setOnClickListener {
            findNavController().navigateUp()
        }







        return binding.root
    }

}
