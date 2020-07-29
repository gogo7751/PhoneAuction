package com.eric.phoneauction.postFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Notification
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.data.WishList
import com.eric.phoneauction.data.remote.PhoneAuctionRemoteDataSource
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class PostViewModel(private val phoneAuctionRepository: PhoneAuctionRepository) : ViewModel() {

    private var _events = MutableLiveData<List<Event>>()

    val events: LiveData<List<Event>>
        get() = _events

    // Handle the error for post
    private val _invalidPost = MutableLiveData<Int>()

    val invalidPost: LiveData<Int>
        get() = _invalidPost

    var wishList = MutableLiveData<WishList>()

    var averagePrice = MutableLiveData<Int>()

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

    fun getWishListFromPost(brand: String, productName: String, storage: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = phoneAuctionRepository.getWishListFromPost(brand, productName, storage, true)

            wishList.value = when (result) {
                is com.eric.phoneauction.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is com.eric.phoneauction.data.Result.Fail -> {
                    Log.d("Result","fail")
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is com.eric.phoneauction.data.Result.Error -> {
                    Log.d("Result","error")
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    fun getNotification(title: String): Notification {
        return Notification(
            id = "",
            title = title,
            time = -1,
            brand = brand.value.toString(),
            name = productName.value.toString(),
            image = image1.value.toString(),
            storage = storage.value.toString(),
            visibility = true,
            event = getEvent()
        )
    }

    fun postNotification(notification: Notification, buyUser: String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.postNotification(notification, buyUser)) {
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

    fun getAveragePriceResult(brand: String, productName: String, storage: String, deal: Boolean) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = phoneAuctionRepository.getAveragePrice(brand, productName, storage, deal)

            _events.value = when (result) {
                is com.eric.phoneauction.data.Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is com.eric.phoneauction.data.Result.Fail -> {
                    Log.d("Result","fail")
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is com.eric.phoneauction.data.Result.Error -> {
                    Log.d("Result","error")
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }

    fun getEvent(): Event {
        val images = mutableListOf<String>()
        if (image1.value != null) { images.add(image1.value.toString()) }
        if (image2.value != null) { images.add(image2.value.toString()) }
        if (image3.value != null) { images.add(image3.value.toString()) }
        if (image4.value != null) { images.add(image4.value.toString()) }
        if (image5.value != null) { images.add(image5.value.toString()) }

        val events = FirebaseFirestore.getInstance().collection("events")
        val document = events.document()
        return Event(
            id = document.id,
            productName = productName.value.toString(),
            storage = storage.value.toString(),
            brand = brand.value.toString(),
            price = price.value?.toInt() as Int,
            images = images,
            trade = trade.value.toString(),
            description = description.value.toString(),
            endTime = Calendar.getInstance().timeInMillis + 259200000,
            createdTime = Calendar.getInstance().timeInMillis,
            tag = tag.value.toString(),
            userId = UserManager.userId.toString(),
            buyUser = "",
            sellerImage = UserManager.user.image,
            sellerName = UserManager.user.name,
            deal = true
        )
    }

    fun preparePost() {
        when {
            brand.value == "0" -> _invalidPost.value = INVALID_FORMAT_BRAND_EMPTY
            productName.value == "0" -> _invalidPost.value = INVALID_FORMAT_PRODUCT_NAME_EMPTY
            storage.value == "0" -> _invalidPost.value = INVALID_FORMAT_STORAGE_EMPTY
            description.value.isNullOrEmpty() -> _invalidPost.value = INVALID_FORMAT_DESCRIPTION_EMPTY
            tag.value.isNullOrEmpty() -> _invalidPost.value = INVALID_FORMAT_TAG_EMPTY
            price.value.isNullOrEmpty() -> _invalidPost.value = INVALID_FORMAT_PRICE_EMPTY
            image1.value.isNullOrEmpty() -> _invalidPost.value = INVALID_FORMAT_IMAGE_EMPTY
            trade.value.isNullOrEmpty() -> _invalidPost.value = INVALID_FORMAT_TRADE_EMPTY
            else -> {
                post(getEvent())
                getWishListFromPost(brand.value.toString(), productName.value.toString(), storage.value.toString())
            }
        }
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

    companion object {

        const val INVALID_FORMAT_IMAGE_EMPTY        = 11
        const val INVALID_FORMAT_BRAND_EMPTY        = 12
        const val INVALID_FORMAT_PRODUCT_NAME_EMPTY = 13
        const val INVALID_FORMAT_STORAGE_EMPTY      = 14
        const val INVALID_FORMAT_PRICE_EMPTY        = 15
        const val INVALID_FORMAT_TAG_EMPTY          = 16
        const val INVALID_FORMAT_TRADE_EMPTY        = 17
        const val INVALID_FORMAT_DESCRIPTION_EMPTY  = 18
        const val NO_ONE_KNOWS                      = 21

    }
}

