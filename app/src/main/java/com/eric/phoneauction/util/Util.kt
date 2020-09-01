package com.eric.phoneauction.util

import com.eric.phoneauction.PhoneAuctionApplication

object Util {

    fun getColor(resourceId: Int): Int {
        return PhoneAuctionApplication.instance.getColor(resourceId)
    }

    fun lessThenTenPadStart(time: Long): String {
        return if (time < 10) {
            time.toString().padStart(2,'0')
        } else time.toString()
    }

    fun getString(resourceId: Int): String {
        return PhoneAuctionApplication.instance.getString(resourceId)
    }


}