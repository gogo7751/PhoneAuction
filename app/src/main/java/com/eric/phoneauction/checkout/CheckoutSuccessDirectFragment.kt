package com.eric.phoneauction.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eric.phoneauction.NavigationDirections

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentCheckoutSuccessDirectBinding
import com.eric.phoneauction.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple [Fragment] subclass.
 */
class CheckoutSuccessDirectFragment : Fragment() {

    private val viewModel by viewModels<CheckoutSuccessViewModel> { getVmFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCheckoutSuccessDirectBinding.inflate(inflater, container, false)



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
