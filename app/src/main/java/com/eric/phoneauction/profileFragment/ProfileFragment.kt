package com.eric.phoneauction.profileFragment

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
            Toast.makeText(context, "問與答 coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.buttonProfilePolicy.setOnClickListener {
            Toast.makeText(context, "使用規範 coming soon", Toast.LENGTH_SHORT).show()
            addData()
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
        productName = "Xperia 1 II",
        storage = "64G",
        brand = "Sony",
        price = 9000,
        images = listOf("https://firebasestorage.googleapis.com/v0/b/phoneauction-e97b4.appspot.com/o/images%2Fe4cbde0d-d106-43ef-826d-45e8a709e240?alt=media&token=43098544-a4bd-4039-9547-18ea6db24213"),
        trade = "面交",
        description = "無聊",
        endTime = Calendar.getInstance().timeInMillis + 259200000,
        createdTime = Calendar.getInstance().timeInMillis,
        tag = "拍賣",
        userId = "II9r5OGlAFfCGowS7EpRQIrdLr32",
        buyUser = "",
        sellerName = "Lin",
        sellerImage = "https://graph.facebook.com/10157422827030994/picture",
        deal = true
    )
    document.set(event)
}