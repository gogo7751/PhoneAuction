package com.eric.phoneauction

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ActivityMainBinding
import com.eric.phoneauction.databinding.BadgeBottomBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.ext.showToast
import com.eric.phoneauction.notification.NotificationViewModel
import com.eric.phoneauction.util.Logger
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.system.exitProcess


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainViewModel> { getVmFactory() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupBottomNav()
        viewModel.postUser(UserManager.user)
        viewModel.getUser()

        val notificationViewModel: NotificationViewModel by viewModels { getVmFactory() }

        notificationViewModel.getLiveNotificationsResult()
        notificationViewModel.liveNotifications.observe(this, androidx.lifecycle.Observer {
            it?.let {
                viewModel.notifications.value = it
            }
        })

        viewModel.user.observe(this, androidx.lifecycle.Observer {
            it?.let {
                UserManager.user = it
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

        val menuView = binding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(3) as BottomNavigationItemView
        val bindingBadge = BadgeBottomBinding.inflate(LayoutInflater.from(this), itemView, true)
        bindingBadge.lifecycleOwner = this
        bindingBadge.viewModel = viewModel
    }
}
