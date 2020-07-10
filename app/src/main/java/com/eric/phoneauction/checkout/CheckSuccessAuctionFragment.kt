package com.eric.phoneauction.checkout

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.MainViewModel
import com.eric.phoneauction.NavigationDirections

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentCheckoutSuccessAuctionBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.util.Logger.d
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass.
 */
class CheckSuccessAuctionFragment : Fragment() {

    private val viewModel by viewModels<CheckoutSuccessViewModel> { getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCheckoutSuccessAuctionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
                viewModel.onHomeNavigated()
            }
        })




        return binding.root
    }


}
