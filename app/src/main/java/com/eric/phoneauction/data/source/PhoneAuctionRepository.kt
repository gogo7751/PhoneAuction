package com.eric.phoneauction.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Result


/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Interface to the Publisher layers.
 */
interface PhoneAuctionRepository {

    suspend fun getEvents(): Result<List<Event>>

    fun getLiveEvent(): MutableLiveData<List<Event>>


}
