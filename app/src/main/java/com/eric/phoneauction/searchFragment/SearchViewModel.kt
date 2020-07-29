package com.eric.phoneauction.searchFragment

import android.util.EventLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.UserManager
import com.eric.phoneauction.data.WishList
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    val phoneAuctionRepository: PhoneAuctionRepository,
    val search: String
) : ViewModel() {

    var liveSearchs = MutableLiveData<List<Event>>()

    val editSearch = MutableLiveData<String>()

    val wishList = MutableLiveData<WishList>().apply {
        value = WishList()
    }

    var isVisibility = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val _navigateToDetail = MutableLiveData<Event>()

    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

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
        getLiveSearchResult("brand" ,search)
        wishList.value?.userId = UserManager.userId.toString()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getLiveSearchResult(field: String, search: String) {
        liveSearchs = phoneAuctionRepository.getLiveSearch(field, search)
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

    fun postWishList(wishList: WishList) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.postWishList(wishList)) {
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

    fun navigateToDetail(event: Event) {
        _navigateToDetail.value = event
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }

    fun getVisibility() {
        isVisibility.value = true
    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }

}