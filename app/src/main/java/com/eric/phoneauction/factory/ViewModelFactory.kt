package com.eric.phoneauction.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eric.phoneauction.MainViewModel
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.homeFragment.HomeViewModel
import com.eric.phoneauction.postFragment.PostViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val phoneAuctionRepository: PhoneAuctionRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(phoneAuctionRepository)

                isAssignableFrom(PostViewModel::class.java) ->
                    PostViewModel(phoneAuctionRepository)

                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(phoneAuctionRepository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}