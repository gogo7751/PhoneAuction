package com.eric.phoneauction.chatToChatDetail

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.ChatRoom
import com.eric.phoneauction.data.Message
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatToDetailChatViewModel(
    private val phoneAuctionRepository: PhoneAuctionRepository,
    private val arguments: ChatRoom
) : ViewModel() {

    private val _chatRoom = MutableLiveData<ChatRoom>().apply {
        value = arguments
    }

    val chatRoom: LiveData<ChatRoom>
        get() = _chatRoom

    private val _sendImage = MutableLiveData<String>()

    val sendImage: LiveData<String>
        get() = _sendImage

    var liveMessages = MutableLiveData<List<Message>>()

    private val _message = MutableLiveData<Message>().apply {
        value = Message()
    }

    val message: LiveData<Message>
        get() = _message

    private var _isTitleName = MutableLiveData<Boolean>()

    val isTitleName: LiveData<Boolean>
        get() = _isTitleName

    private var _setEditText = MutableLiveData<Boolean>()

    val setEditText: LiveData<Boolean>
        get() = _setEditText

    // Handle leave
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

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
        _message.value?.senderImage = UserManager.user.image
        _message.value?.id = UserManager.userId.toString()
        getLiveMessagesResult()
        _isTitleName.value = chatRoom.value?.senderId == UserManager.userId
    }

    fun isUserManager(id: String): Boolean{
        return id == UserManager.userId
    }

    private fun getLiveMessagesResult() {
        liveMessages = phoneAuctionRepository.getLiveMessage(chatRoom.value?.id.toString())
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

    fun postMessage(message: Message, document: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.postMessage(message, document)) {
                is com.eric.phoneauction.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    _setEditText.value = true
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

    fun uploadImage(saveUri: Uri) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.uploadImage(_sendImage, saveUri)) {
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

    fun sendImage(image: String) {
        _message.value?.image = image
        _message.value?.text = ""
        _message.value?.let {message -> chatRoom.value?.id.let {
                chatRoom -> postMessage(message, chatRoom as String) } }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun leave() {
        _leave.value = true
    }

    fun clearEditText() {
        _setEditText.value = null
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }
}