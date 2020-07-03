package com.eric.phoneauction.ext

import androidx.fragment.app.Fragment
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.factory.ViewModelFactory

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Extension functions for Fragment.
 *
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as PhoneAuctionApplication).repository
    return ViewModelFactory(repository)
}