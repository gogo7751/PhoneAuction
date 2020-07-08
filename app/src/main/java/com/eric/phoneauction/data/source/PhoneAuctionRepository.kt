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

    suspend fun post(event: Event): Result<Boolean>

    suspend fun getAuction(): Result<List<Event>>

    suspend fun getDirect(): Result<List<Event>>

}
