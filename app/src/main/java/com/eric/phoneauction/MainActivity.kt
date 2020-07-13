package com.eric.phoneauction

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ActivityMainBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.homeFragment.HomeViewModel
import com.eric.phoneauction.util.Logger
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class MainActivity : AppCompatActivity() {


    val viewModel by viewModels<MainViewModel> { getVmFactory() }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel
        setupBottomNav()
        viewModel.postUser(UserManager.user)
        viewModel.getUser()

        viewModel.user.observe(this, androidx.lifecycle.Observer {
            it?.let {
                UserManager.user = it
                Logger.d("123456789$it")
            }
        })
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






}
