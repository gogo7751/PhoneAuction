package com.eric.phoneauction.detailAuctionFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.DetailAuctionFragmentBinding

class DetailAuctionFragment : Fragment() {

    lateinit var binding: DetailAuctionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailAuctionFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }



}
