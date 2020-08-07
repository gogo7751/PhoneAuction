package com.eric.phoneauction.onPost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class OnPostViewModel(val phoneAuctionRepository: PhoneAuctionRepository) : ViewModel() {

    var liveEvents = MutableLiveData<List<Event>>()

    var isEmpty = MutableLiveData<Boolean>()

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

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
        getLiveEventsResult()
    }

    fun getLiveEventsResult() {
        liveEvents = phoneAuctionRepository.getLiveEvent(true)
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

}