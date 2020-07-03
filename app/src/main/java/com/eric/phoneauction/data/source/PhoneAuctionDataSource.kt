package com.eric.phoneauction.data.source

import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Result


/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Main entry point for accessing Publisher sources.
 */
interface PhoneAuctionDataSource {

    suspend fun getEvents(): Result<List<Event>>

    fun getLiveEvent(): MutableLiveData<List<Event>>


}
