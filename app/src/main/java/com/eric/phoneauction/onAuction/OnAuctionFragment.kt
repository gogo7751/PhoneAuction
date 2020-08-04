package com.eric.phoneauction.onAuction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.eric.phoneauction.data.UserManager
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
                if (it.map { it.buyUser }.equals(UserManager.userId)) {
                    binding.textNoContent.visibility = View.GONE
                    adapter.submitList(it)
                } else {
                    binding.textNoContent.visibility = View.VISIBLE
                }
            }
        })

        binding.imageOnAuctionBack.setOnClickListener {
            findNavController().navigateUp()
        }







        return binding.root
    }

}
