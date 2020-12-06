package com.eric.phoneauction.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.eric.phoneauction.R

import com.eric.phoneauction.data.Event
import com.eric.phoneauction.databinding.FragmentProfileBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.showToast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class ProfileFragment : Fragment() {

    val viewModel: ProfileViewModel by viewModels { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.buttonProfileRecently.setOnClickListener {
            activity.showToast(getString(R.string.recently_viewed))
        }

        binding.buttonProfileQuestion.setOnClickListener {
            activity.showToast(getString(R.string.information))
        }

        binding.buttonProfilePolicy.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToProfilePolicyFragment())
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse(getString(R.string.privacy_policy))
//            startActivity(intent)
        }

        binding.buttonProfilePurchased.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPurchasedFragment())
        }

        binding.buttonProfileBid.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToOnAuctionFragment())
        }

        binding.buttonProfileUpload.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToOnPostFragment())
        }

        binding.buttonProfileCollection.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToCollectionFragment())
        }

        binding.buttonProfileWish.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWishListFragment())
        }

        (activity as AppCompatActivity).bottomNavView.visibility = View.VISIBLE
        return binding.root
    }
}