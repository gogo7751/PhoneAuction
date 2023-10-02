package com.eric.phoneauction.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eric.phoneauction.chatToChatDetailFragment.ChatToDetailChatViewModel
import com.eric.phoneauction.data.ChatRoom
import com.eric.phoneauction.data.source.PhoneAuctionRepository

@Suppress("UNCHECKED_CAST")
class ChatViewModelFactory constructor(
    private val phoneAuctionRepository: PhoneAuctionRepository,
    private val chatRoom: ChatRoom
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(ChatToDetailChatViewModel::class.java) ->
                    ChatToDetailChatViewModel(phoneAuctionRepository, chatRoom)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}