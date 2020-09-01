package com.eric.phoneauction.ext

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.data.ChatRoom
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.factory.ChatViewModelFactory
import com.eric.phoneauction.factory.EventViewModelFactory
import com.eric.phoneauction.factory.SearchViewModelFactory
import com.eric.phoneauction.factory.ViewModelFactory

/**
 * Created by Eric Chang in Jul. 2020.
 *
 * Extension functions for Fragment.
 *
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as PhoneAuctionApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(event: Event): EventViewModelFactory {
    val repository = (requireContext().applicationContext as PhoneAuctionApplication).repository
    return EventViewModelFactory(repository, event)
}

fun Fragment.getVmFactory(chatRoom: ChatRoom): ChatViewModelFactory {
    val repository = (requireContext().applicationContext as PhoneAuctionApplication).repository
    return ChatViewModelFactory(repository, chatRoom)
}

fun Fragment.getVmFactory(search: String?): SearchViewModelFactory {
    val repository = (requireContext().applicationContext as PhoneAuctionApplication).repository
    return SearchViewModelFactory(repository, search as String)
}

fun Fragment.permission() {
        val permissionList = arrayListOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        var size = permissionList.size
        var i = 0
        while (i < size) {
            if (ActivityCompat.checkSelfPermission(PhoneAuctionApplication.instance.applicationContext, permissionList[i]) == PackageManager.PERMISSION_GRANTED) {
                permissionList.removeAt(i)
                i -= 1
                size -= 1
            }
            i += 1
        }
        val array = arrayOfNulls<String>(permissionList.size)
        if (permissionList.isNotEmpty()) ActivityCompat.requestPermissions((activity as AppCompatActivity), permissionList.toArray(array), 0)
}



