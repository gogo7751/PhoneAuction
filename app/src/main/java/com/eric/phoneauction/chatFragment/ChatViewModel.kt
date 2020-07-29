package com.eric.phoneauction.chatFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.ChatRoom
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Notification
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatViewModel(
    val phoneAuctionRepository: PhoneAuctionRepository
) : ViewModel() {

    var liveChatRooms = MutableLiveData<List<ChatRoom>>()

    var isEmpty = MutableLiveData<Boolean>()

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

    fun deleteChatRoom(charRoomId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.deleteChatRoom(charRoomId)) {
                is com.eric.phoneauction.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                }
                is com.eric.phoneauction.data.Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is com.eric.phoneauction.data.Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
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
