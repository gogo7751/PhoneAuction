package com.eric.phoneauction.auctionDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Notification
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import com.eric.phoneauction.util.Util.getString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AuctionViewModel(
    val phoneAuctionRepository: PhoneAuctionRepository,
    arguments: Event
) : ViewModel() {

    private val _event = MutableLiveData<Event>().apply {
        value = arguments
    }

    val event: LiveData<Event>
        get() = _event

    val isBuyerId = _event.value?.buyerId == UserManager.userId
    val isSellerId = _event.value?.sellerId == UserManager.userId

    private val _notification = MutableLiveData<Notification>().apply {
        value = Notification()
    }

    val notification: LiveData<Notification>
        get() = _notification

    val price = MutableLiveData<Int>().apply {
        value = _event.value?.price
    }

    // Handle leave auction
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // Handle navigation to checkoutSuccess
    private val _navigateToCheckout = MutableLiveData<Event>()

    val navigateToCheckout: LiveData<Event>
        get() = _navigateToCheckout

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
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getNotificationData(title: String): Notification{
        return Notification(
                "",
                title,
                -1,
                event.value?.brand.toString(),
                event.value?.productName.toString(),
                event.value?.images?.component1().toString(),
                event.value?.storage.toString(),
                true,
                event.value.apply { event.value?.price = price.value as Int }
        )
    }

    private fun postNotification(notification: Notification, userId: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.postNotification(notification, userId)) {
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

    private fun postAuction(event: Event, price: Int) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.postAuction(event, price)) {
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

    fun addMinimalPrice(originPrice: Int) {
        price.value = originPrice.times(1.01).toInt()
    }

    fun addPrice(newPrice: Int) {
        price.value = price.value?.plus(newPrice)
    }

    fun navigateToCheckout(event: Event) {
        if (event.buyerId != "") {
            postNotification(getNotificationData(getString(R.string.bid_over)), event.buyerId)
        }
        postAuction(event, price.value as Int)
        postNotification(getNotificationData(getString(R.string.bid_someone)), event.sellerId)
        _navigateToCheckout.value = event
    }

    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }

    fun nothing() {}
}