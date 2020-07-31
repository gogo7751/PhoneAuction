package com.eric.phoneauction.ext

import androidx.fragment.app.Fragment
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.data.ChatRoom
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.factory.ChatViewModelFactory
import com.eric.phoneauction.factory.EventViewModelFactory
import com.eric.phoneauction.factory.SearchViewModelFactory
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



