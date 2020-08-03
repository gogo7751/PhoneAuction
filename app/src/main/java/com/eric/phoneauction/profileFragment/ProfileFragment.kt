package com.eric.phoneauction.profileFragment

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

import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.databinding.FragmentProfileBinding
import com.eric.phoneauction.ext.getVmFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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
            FirebaseFirestore.getInstance().collection("events").document().update("buyUser", "", "deal", "")
        }

        binding.buttonProfileQuestion.setOnClickListener {
            Toast.makeText(context, "使用說明 coming soon", Toast.LENGTH_SHORT).show()
            addData()
        }

        binding.buttonProfilePolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://docs.google.com/document/d/1fS0I6PN980hEX1K5jTg5cjInUaPvNTub05IfiJVsXok/edit?usp=sharing")
            startActivity(intent)
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

fun addData() {
    val events = FirebaseFirestore.getInstance()
        .collection("events")
    val document = events.document()
    val event = Event(
        id = document.id,
        productName = "Xperia 1",
        storage = "64G",
        brand = "Sony",
        price = 8000,
        images = listOf("https://firebasestorage.googleapis.com/v0/b/phoneauction-e97b4.appspot.com/o/images%2F890f8339-6795-4c41-9063-1797a7374904?alt=media&token=c6c381bd-a9d1-494b-970d-1a15cac7ee6a"),
        trade = "面交",
        description = "9成新,功能正常外觀良好",
        endTime = Calendar.getInstance().timeInMillis + 259200000,
        createdTime = Calendar.getInstance().timeInMillis,
        tag = "拍賣",
        userId = "II9r5OGlAFfCGowS7EpRQIrdLr32",
        buyUser = "",
        sellerName = "Dato Chang",
        sellerImage = "https://graph.facebook.com/10157422827030994/picture",
        deal = true
    )
    document.set(event)
}