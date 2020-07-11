package com.eric.phoneauction.profileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.eric.phoneauction.R
import com.eric.phoneauction.databinding.FragmentProfileBinding
import com.eric.phoneauction.ext.getVmFactory

class ProfileFragment : Fragment() {

    val viewModel: ProfileViewModel by viewModels<ProfileViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel










        return binding.root
    }


}
