package com.eric.phoneauction.chatFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.data.ChatRoom
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Notification
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ChatViewModel(
    val phoneAuctionRepository: PhoneAuctionRepository
) : ViewModel() {

    var liveChatRooms = MutableLiveData<List<ChatRoom>>()

    // Handle navigation to detail
    private val _navigateToChatToDetail = MutableLiveData<ChatRoom>()

    val navigateToChatToDetail: LiveData<ChatRoom>
        get() = _navigateToChatToDetail

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
        getLiveChatRoomsResult()

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun getLiveChatRoomsResult() {
        liveChatRooms = phoneAuctionRepository.getLiveChatRoom()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

    fun navigateToChatToDetail(chatRoom: ChatRoom) {
        _navigateToChatToDetail.value = chatRoom
    }

    fun onChatToDetailNavigated() {
        _navigateToChatToDetail.value = null
    }


}
