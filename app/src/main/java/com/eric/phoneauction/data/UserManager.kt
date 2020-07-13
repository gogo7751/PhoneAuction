package com.eric.phoneauction.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.PhoneAuctionApplication

object UserManager {

    var user = User()

    var userId: String?
        get() {
            return  PhoneAuctionApplication.instance.applicationContext.getSharedPreferences("getUserId", Context.MODE_PRIVATE).getString("userId", null)
        }
        set(a){
            PhoneAuctionApplication.instance.applicationContext.getSharedPreferences("getUserId", Context.MODE_PRIVATE).edit().putString("userId", a).apply()
        }

}