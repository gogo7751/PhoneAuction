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

    var buyUser = MutableLiveData<String>()

    private val _notification = MutableLiveData<Notification>()

    val notification: LiveData<Notification>
        get() = _notification

    // Handle leave auction
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    val price = MutableLiveData<Int>().apply {
        value = event.value?.price
    }

    // Handle navigation to checkoutSuccess
    private val _navigateToCheckoutSuccess = MutableLiveData<Event>()

    val navigateToCheckoutSuccess: LiveData<Event>
        get() = _navigateToCheckoutSuccess


    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String?>()

    val error: MutableLiveData<String?>
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
        buyUser.value = event.value?.buyUser
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getNotification(title: String): Notification{
        return Notification(
                id = "",
                title = title,
                time = -1,
                brand = event.value?.brand.toString(),
                name = event.value?.productName.toString(),
                image = event.value?.images?.component1().toString(),
                storage = event.value?.storage.toString(),
                visibility = true,
                event = event.value
        )
    }

    fun postNotification(notification: Notification, user: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.postNotification(notification, user)) {
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


    fun postAuction(event: Event, price: Int) {

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

    fun add100() {
        price.value = price.value?.plus(100)
    }

    fun add300() {
        price.value = price.value?.plus(300)
    }

    fun add500() {
        price.value = price.value?.plus(500)
    }


    fun navigateToCheckoutSuccess(event: Event) {
        _navigateToCheckoutSuccess.value = event
    }



    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }


    fun nothing() {}
}