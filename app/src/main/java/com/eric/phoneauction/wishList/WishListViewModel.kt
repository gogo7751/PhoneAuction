package com.eric.phoneauction.wishList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.WishList
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WishListViewModel(val phoneAuctionRepository: PhoneAuctionRepository) : ViewModel() {

    private var _wishLists = MutableLiveData<List<WishList>>()

    val wishLists: LiveData<List<WishList>>
        get() = _wishLists

    private val _navigateToDialog = MutableLiveData<Boolean>()

    val navigateToDialog: LiveData<Boolean>
        get() = _navigateToDialog

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

    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
        getLiveWishListResult()
    }

    fun getLiveWishListResult() {
        _wishLists = phoneAuctionRepository.getWishList()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
    }

    fun updateWishList(id: String) {

            coroutineScope.launch {

                _status.value = LoadApiStatus.LOADING

                when (val result = phoneAuctionRepository.updateWishList(id)) {
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

    fun navigateToDialog() {
        _navigateToDialog.value = true
    }

    fun onDialogNavigated() {
        _navigateToDialog.value = null
    }

    fun leave(needRefresh: Boolean = false) {
        _leave.value = needRefresh
    }
}