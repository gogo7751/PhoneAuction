package com.eric.phoneauction.profileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

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


        binding.buttonProfileRecently.setOnClickListener {
            Toast.makeText(context, "最近瀏覽 coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.buttonProfileQuestion.setOnClickListener {
            Toast.makeText(context, "問與答 coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.buttonProfilePolicy.setOnClickListener {
            Toast.makeText(context, "使用規範 coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.buttonProfilePurchased.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPurchasedFragment())
        }

        return binding.root
    }


}
