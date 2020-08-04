package com.eric.phoneauction.ext

import android.app.Activity
import android.view.View
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.factory.ViewModelFactory

/**
 * Created by Eric Chang in Jul. 2020.
 *
 * Extension functions for Activity.
 */
fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as PhoneAuctionApplication).repository
    return ViewModelFactory(repository)
}

