package com.eric.phoneauction.data.source

import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Result
import com.eric.phoneauction.data.User


/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Main entry point for accessing Publisher sources.
 */
interface PhoneAuctionDataSource {

    suspend fun getEvents(): Result<List<Event>>

    fun getLiveEvent(): MutableLiveData<List<Event>>

    suspend fun getUser(): Result<User>

    suspend fun getAuction(): Result<List<Event>>

    suspend fun getDirect(): Result<List<Event>>

    suspend fun post(event: Event): Result<Boolean>

    suspend fun postUser(user: User): Result<Boolean>

    suspend fun postAuction(event: Event, price: Int): Result<Boolean>

    suspend fun postDirect(event: Event): Result<Boolean>

}
