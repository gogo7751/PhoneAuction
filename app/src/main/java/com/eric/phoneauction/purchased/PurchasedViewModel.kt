package com.eric.phoneauction.purchased

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class PurchasedViewModel(val phoneAuctionRepository: PhoneAuctionRepository) : ViewModel() {

    var liveEvents = MutableLiveData<List<Event>>()

    private val _isNotEmpty = MutableLiveData<Boolean>()

    val isNotEmpty: LiveData<Boolean>
        get() = _isNotEmpty

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

    fun isNotEmpty(event: List<Event>){
        _isNotEmpty.value = event.map { it.buyerId }.contains(UserManager.userId)
    }

    private fun getLiveEventsResult() {
        liveEvents = phoneAuctionRepository.getLiveEvent(false)
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }
}