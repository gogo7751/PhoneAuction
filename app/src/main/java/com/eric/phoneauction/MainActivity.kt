package com.eric.phoneauction

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.eric.phoneauction.databinding.ActivityMainBinding
import com.eric.phoneauction.ext.getVmFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel
//        addData()
        setupBottomNav()
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chat -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalChatFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_post -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalPostFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notification -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalNotificationFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setupBottomNav() {
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

//    fun addData() {
//        val events = FirebaseFirestore.getInstance()
//            .collection("events")
//
//        val document = events.document()
//
//        val data = hashMapOf(
//            "brand" to "Samsung",
//            "description" to "紫色，9成新，女用機",
//            "createdTime" to Calendar.getInstance().timeInMillis,
//            "endTime" to Calendar.getInstance().timeInMillis + 259200,
//            "id" to document.id,
//            "tag" to "direct",
//            "images" to "https://d2482qdi0l0aam.cloudfront.net/assets/201807242228/0.jpg",
//            "price" to 10000,
//            "productName" to "S20 Ultra",
//            "trade" to "面交",
//            "storage" to "32G"
//            "userId" to "gogo7751"
//        )
//        document.set(data)
//    }




}
