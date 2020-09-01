package com.eric.phoneauction

import android.app.Application
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.ServiceLocator
import kotlin.properties.Delegates

class PhoneAuctionApplication : Application() {

    val repository: PhoneAuctionRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: PhoneAuctionApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun isLiveDataDesign() = true
}