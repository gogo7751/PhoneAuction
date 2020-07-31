package com.eric.phoneauction

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.databinding.ActivityMainBinding
import com.eric.phoneauction.databinding.BadgeBottomBinding
import com.eric.phoneauction.ext.getVmFactory
import com.eric.phoneauction.homeFragment.HomeViewModel
import com.eric.phoneauction.notificationFragment.NotificationViewModel
import com.eric.phoneauction.util.Logger
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView
import java.util.*
import kotlin.system.exitProcess


@Suppress("DEPRECATED_IDENTITY_EQUALS")
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

        val notificationViewModel: NotificationViewModel by viewModels { getVmFactory() }

        notificationViewModel.getLiveNotificationsResult()
        notificationViewModel.liveNotifications.observe(this, androidx.lifecycle.Observer {
            viewModel.notifications.value = notificationViewModel.liveNotifications.value
        })

        viewModel.user.observe(this, androidx.lifecycle.Observer {
            it?.let {
                UserManager.user = it
                Logger.d("123456789$it")
            }
        })
    }


    var exitTime:Long = 0
    override fun onKeyDown(keyCode:Int, event: KeyEvent):Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() === KeyEvent.ACTION_DOWN)
        {
            if ((System.currentTimeMillis() - exitTime) > 3000)
            {
                Toast.makeText(applicationContext, "再按一次退出程式", Toast.LENGTH_SHORT).show()
                exitTime = System.currentTimeMillis()
            }
            else
            {
                finish()
                exitProcess(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
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
        binding.bottomNavView.itemBackground = null

        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val menuView = binding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(3) as BottomNavigationItemView
        val bindingBadge = BadgeBottomBinding.inflate(LayoutInflater.from(this), itemView, true)
        bindingBadge.lifecycleOwner = this
        bindingBadge.viewModel = viewModel
    }






}
