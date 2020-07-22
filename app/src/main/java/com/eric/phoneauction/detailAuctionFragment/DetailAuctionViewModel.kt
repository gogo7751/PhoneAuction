package com.eric.phoneauction.detailAuctionFragment

import android.graphics.Rect
import android.os.CountDownTimer
import android.os.UserManager
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.publisher.network.LoadApiStatus
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.*
import com.eric.phoneauction.data.Collection
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.homeFragment.HomeAdapter
import com.eric.phoneauction.homeFragment.HomeViewModel
import com.eric.phoneauction.util.Logger
import com.eric.phoneauction.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailAuctionViewModel(
    private val phoneAuctionRepository: PhoneAuctionRepository,
    private val arguments: Event
) : ViewModel() {

    private val _event = MutableLiveData<Event>().apply {
        value = arguments
    }

    val event: LiveData<Event>
        get() = _event


    private var _events = MutableLiveData<List<Event>>()

    val events: LiveData<List<Event>>
        get() = _events

    var collection = MutableLiveData<Collection>()


    val isBuyUser = MutableLiveData<Boolean>().apply {
        value = arguments.buyUser == arguments.userId
    }

    lateinit var timer: CountDownTimer
    val countDown = MutableLiveData<String>()

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Event>()

    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail

    // Handle navigation to auction
    private val _navigateToAuction = MutableLiveData<Event>()

    val navigateToAuction: LiveData<Event>
        get() = _navigateToAuction

    // Handle navigation to detail chat
    private val _navigateToDetailChat = MutableLiveData<Event>()

    val navigateToDetailChat: LiveData<Event>
        get() = _navigateToDetailChat

    // it for gallery circles design
    private val _snapPosition = MutableLiveData<Int>()

    val snapPosition: LiveData<Int>
        get() = _snapPosition

    // Handle leave detail
    private val _leaveDetail = MutableLiveData<Boolean>()

    val leaveDetail: LiveData<Boolean>
        get() = _leaveDetail

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

    val decoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0
            } else {
                outRect.left = PhoneAuctionApplication.instance.resources.getDimensionPixelSize(R.dimen.space_detail_circle)
            }
        }
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
        getAuctionResult()
        getCountdown()
        getCollectionResult()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getCollectionResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = phoneAuctionRepository.getCollection(arguments.id)

            collection.value = when (result) {
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


    fun addCollection(boolean: Boolean) :Collection{
        return Collection(
            id = "",
            visibility = boolean,
            event = arguments
        )
    }

    fun postCollection(collection: Collection, user: User) {
        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.postCollection(collection, user)) {
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

    fun getChatRoom(): ChatRoom{
        val list = listOf<String>(event.value?.userId.toString(), com.eric.phoneauction.data.UserManager.user.id)
        return ChatRoom(
            id = event.value?.id.toString() + com.eric.phoneauction.data.UserManager.userId,
            text = "查看訊息",
            time = -1,
            senderName = com.eric.phoneauction.data.UserManager.user.name,
            senderImage = com.eric.phoneauction.data.UserManager.user.image,
            senderId = com.eric.phoneauction.data.UserManager.user.id,
            receiverId = event.value?.userId.toString(),
            receiverImage = event.value?.sellerImage.toString(),
            receiverName = event.value?.sellerName.toString(),
            productImage = event.value?.images?.component1().toString(),
            visibility = true,
            event = event.value
        )
    }


    fun postChatRoom(chatRoom: ChatRoom) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.postChatRoom(chatRoom)) {
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


    /**
     * When the gallery scroll, at the same time circles design will switch.
     */
    fun onGalleryScrollChange(layoutManager: RecyclerView.LayoutManager?, linearSnapHelper: LinearSnapHelper) {
        val snapView = linearSnapHelper.findSnapView(layoutManager)
        snapView?.let {
            layoutManager?.getPosition(snapView)?.let {
                if (it != snapPosition.value) {
                    _snapPosition.value = it
                }
            }
        }
    }

    fun getAuctionResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = phoneAuctionRepository.getAuction()

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

    fun finishAuction(event: Event) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = phoneAuctionRepository.finishAuction(event)) {
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

    fun getNotification(title: String): Notification {
        return Notification(
            id = "",
            title = title,
            time = -1,
            brand = event.value?.brand.toString(),
            name = event.value?.productName.toString(),
            image = event.value?.images?.component1().toString(),
            storage = event.value?.storage.toString(),
            visibility = true,
            event = event.value.apply { event.value?.deal = false }
        )
    }

    fun getCountdown() {
        timer = object : CountDownTimer(arguments.endTime.minus(arguments.createdTime), HomeAdapter.ONE_SECOND) {
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished / HomeAdapter.ONE_SECOND % 60
                val min = millisUntilFinished / HomeAdapter.ONE_SECOND / 60 % 60
                val hr = millisUntilFinished / HomeAdapter.ONE_SECOND / 60 / 60 % 24
                countDown.value = StringBuilder()
                    .append(Util.lessThenTenPadStart(hr)).append(Util.getString(R.string.hours))
                    .append(Util.lessThenTenPadStart(min)).append(Util.getString(R.string.minutes))
                    .append(Util.lessThenTenPadStart(sec)).append(Util.getString(R.string.seconds)).toString()
            }
        }
    }

    fun timerStart(){
        timer.start()
    }

    fun timerStop(){
        timer.cancel()
    }


    fun navigateToAuction(event: Event) {
        _navigateToAuction.value = event
    }

    fun onAuctionNavigated() {
        _navigateToAuction.value = null
    }

    fun navigateToDetail(event: Event) {
        _navigateToDetail.value = event
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }

    fun navigateToDetailChat(event: Event) {
        _navigateToDetailChat.value = event
    }

    fun onDetailChatNavigated() {
        _navigateToDetailChat.value = null
    }

    fun leaveDetail() {
        _leaveDetail.value = true
    }
}
