package com.eric.phoneauction.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.eric.phoneauction.data.source.DefaultPhoneAuctionRepository
import com.eric.phoneauction.data.source.PhoneAuctionDataSource
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.data.local.PhoneAuctionLocalDataSource
import com.eric.phoneauction.data.remote.PhoneAuctionRemoteDataSource

object ServiceLocator {

    @Volatile
    var repository: PhoneAuctionRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): PhoneAuctionRepository {
        synchronized(this) {
            return repository
                ?: repository
                ?: createPhoneAuctionRepository(context)
        }
    }

    private fun createPhoneAuctionRepository(context: Context): PhoneAuctionRepository {
        return DefaultPhoneAuctionRepository(
            PhoneAuctionRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): PhoneAuctionDataSource {
        return PhoneAuctionLocalDataSource(context)
    }
}