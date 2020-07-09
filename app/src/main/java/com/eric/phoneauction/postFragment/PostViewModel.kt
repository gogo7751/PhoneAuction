package com.eric.phoneauction.postFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostViewModel(private val phoneAuctionRepository: PhoneAuctionRepository) : ViewModel() {

//    private val _event = MutableLiveData<Event>()
//
//    val event: LiveData<Event>
//        get() = _event

    val event = MutableLiveData<Event>()

    var productName = MutableLiveData<String>()
    var storage = MutableLiveData<String>()
    var brand = MutableLiveData<String>()
    var price = MutableLiveData<String>()
    var image1 = MutableLiveData<String>()
    var image2 = MutableLiveData<String>()
    var image3 = MutableLiveData<String>()
    var image4 = MutableLiveData<String>()
    var image5 = MutableLiveData<String>()
    var trade = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var tag = MutableLiveData<String>()

    fun getEvent(): Event {
        val images = listOf<String>(
            image1.value.toString(),
            image2.value.toString(),
            image3.value.toString(),
            image4.value.toString(),
            image5.value.toString()
        )
        return Event(
            id = "",
            productName = productName.value.toString(),
            storage = storage.value.toString(),
            brand = brand.value.toString(),
            price = price.value!!.toInt(),
            images = images,
            trade = trade.value.toString(),
            description = description.value.toString(),
            endTime = -1,
            createdTime = -1,
            tag = tag.value.toString(),
            userId = "gogo7751"
        )
    }

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

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
    }

    fun post(event: Event) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.post(event)) {
                is com.eric.phoneauction.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leave(true)
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

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

    fun onLeft() {
        _leave.value = null
    }
}
