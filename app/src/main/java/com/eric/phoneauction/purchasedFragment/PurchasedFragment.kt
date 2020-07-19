package com.eric.phoneauction.purchasedFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentPurchasedBinding
import com.eric.phoneauction.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class PurchasedFragment : Fragment() {

    val viewModel: PurchasedViewModel by viewModels<PurchasedViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPurchasedBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = PurchasedAdapter()
        binding.recyclerviewPurchased.adapter = adapter

        viewModel.liveEvents.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)

        })

        binding.imagePurchasedBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

}
