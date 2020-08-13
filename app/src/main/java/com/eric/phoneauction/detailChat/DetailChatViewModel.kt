package com.eric.phoneauction.detailChat

import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Message
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailChatViewModel(
    private val phoneAuctionRepository: PhoneAuctionRepository,
    private val arguments: Event
) : ViewModel() {

    private val _event = MutableLiveData<Event>().apply {
        value = arguments
    }

    val event: LiveData<Event>
        get() = _event

    private val _sendImage = MutableLiveData<String>()

    val sendImage: LiveData<String>
        get() = _sendImage

    var liveMessages = MutableLiveData<List<Message>>()

    val message = MutableLiveData<Message>().apply {
        value = Message()
    }

    val document = MutableLiveData<String>().apply {
        value = event.value?.id + UserManager.userId
    }

    private var _setEditText = MutableLiveData<Boolean>()

    val setEditText: LiveData<Boolean>
        get() = _setEditText

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
        message.value?.senderImage = UserManager.user.image
        message.value?.id = UserManager.userId.toString()
        getLiveMessagesResult()
    }

    fun isUserManager(id: String): Boolean{
        return id == UserManager.userId
    }

    fun getLiveMessagesResult() {
        liveMessages = phoneAuctionRepository.getLiveMessage(document.value.toString())
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

    fun setImageValue(image: String) {
        message.value?.image = image
        message.value?.text = ""
        message.value?.let {message -> document.value?.let {
                document -> postMessage(message, document) } }
    }

    fun clearEditText() {
        _setEditText.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}