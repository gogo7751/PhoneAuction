package com.eric.phoneauction

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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
    var backPress = false

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

        viewModel.countInCart.observe(this, Observer {
            viewModel.getBadge(it)
        })
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
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
                    backPress = true
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notification -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalNotificationFragment())
                    viewModel.clearBadge()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        val manager = supportFragmentManager
        val count =
            manager.findFragmentById(R.id.myNavHostFragment)?.childFragmentManager?.backStackEntryCount

        if (backPress) {
            findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
            backPress = false
            return
        }

        if (count == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            showToast(getString(R.string.leave_app))

            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
        }
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
