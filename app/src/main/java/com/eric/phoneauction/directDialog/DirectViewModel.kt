package com.eric.phoneauction.directDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DirectViewModel(
    val phoneAuctionRepository: PhoneAuctionRepository,
    arguments: Event
) : ViewModel() {

    private val _event = MutableLiveData<Event>().apply {
        value = arguments
    }

    val event: LiveData<Event>
        get() = _event

    val freight = MutableLiveData<Int>()

    val totalPrice = MutableLiveData<Int>()

    // Handle leave auction
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave


    // Handle navigation to CheckoutSuccess
    private val _navigateToCheckoutSuccess = MutableLiveData<Event>()

    val navigateToCheckoutSuccess: LiveData<Event>
        get() = _navigateToCheckoutSuccess

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
        getFreight()
        totalPrice.value = freight?.value?.let { _event.value?.price?.plus(it) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun postAuction(event: Event) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.postDirect(event)) {
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


    fun navigateToCheckoutSuccess(event: Event) {
        _navigateToCheckoutSuccess.value = event
    }

    fun getFreight() {
        if (_event.value?.trade == "面交") {
            freight.value = 0
        } else {
            freight.value = 60
        }
    }




    fun leave() {
        _leave.value = true
    }

    fun onLeaveCompleted() {
        _leave.value = null
    }


    fun nothing() {}


}