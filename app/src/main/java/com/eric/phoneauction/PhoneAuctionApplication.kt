package com.eric.phoneauction

import android.app.Application
import com.eric.phoneauction.data.source.PhoneAuctionRepository
import com.eric.phoneauction.util.ServiceLocator
import com.google.firebase.FirebaseApp
import kotlin.properties.Delegates

class PhoneAuctionApplication : Application() {

    // Depends on the flavor,
    val repository: PhoneAuctionRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: PhoneAuctionApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        instance = this
    }

    fun isLiveDataDesign() = true
}