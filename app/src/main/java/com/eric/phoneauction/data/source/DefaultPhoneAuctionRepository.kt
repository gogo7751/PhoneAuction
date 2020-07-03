package com.eric.phoneauction.data.source

import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Result


/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation to load Publisher sources.
 */
class DefaultPhoneAuctionRepository(private val remoteDataSource: PhoneAuctionDataSource,
                                    private val localDataSource: PhoneAuctionDataSource
) : PhoneAuctionRepository {
    override suspend fun getEvents(): Result<List<Event>> {
        return remoteDataSource.getEvents()
    }

    override fun getLiveEvent(): MutableLiveData<List<Event>> {
        return remoteDataSource.getLiveEvent()
    }
}
