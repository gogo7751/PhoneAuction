package com.eric.phoneauction.data.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Result
import com.eric.phoneauction.data.source.PhoneAuctionDataSource

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation of a Publisher source as a db.
 */
class PhoneAuctionLocalDataSource(val context: Context) :
    PhoneAuctionDataSource {

    override suspend fun getEvents(): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getLiveEvent(): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun post(event: Event): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postAuction(event: Event, price: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAuction(): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDirect(): Result<List<Event>> {
        TODO("Not yet implemented")
    }

}
