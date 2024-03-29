package com.eric.phoneauction.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eric.phoneauction.auctionDialog.AuctionViewModel
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.detailAuctionFragment.DetailAuctionViewModel
import com.eric.phoneauction.detailChatFragment.DetailChatViewModel
import com.eric.phoneauction.detailDirectFragment.DetailDirectViewModel
import com.eric.phoneauction.directDialog.DirectViewModel

@Suppress("UNCHECKED_CAST")
class EventViewModelFactory constructor(
    private val phoneAuctionRepository: PhoneAuctionRepository,
    private val event: Event
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(DetailAuctionViewModel::class.java) ->
                    DetailAuctionViewModel(phoneAuctionRepository, event)

                isAssignableFrom(DetailDirectViewModel::class.java) ->
                    DetailDirectViewModel(phoneAuctionRepository, event)

                isAssignableFrom(AuctionViewModel::class.java) ->
                    AuctionViewModel(phoneAuctionRepository, event)

                isAssignableFrom(DirectViewModel::class.java) ->
                    DirectViewModel(phoneAuctionRepository, event)

                isAssignableFrom(DetailChatViewModel::class.java) ->
                    DetailChatViewModel(phoneAuctionRepository, event)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}