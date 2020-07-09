package com.eric.phoneauction.util

import android.annotation.SuppressLint
import com.eric.phoneauction.PhoneAuctionApplication
import java.text.SimpleDateFormat
import java.util.*

object Util {

    fun getColor(resourceId: Int): Int {
        return PhoneAuctionApplication.instance.getColor(resourceId)
    }

//    @SuppressLint("SimpleDateFormat")
//    @JvmStatic
//    fun StampToDate(time: Long): String {
//        // 進來的time以秒為單位，Date輸入為毫秒為單位，要注意
//
//        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
//
//        return simpleDateFormat.format(Date(time))
//    }
//
//    @JvmStatic
//    fun DateToStamp(date: String, locale: Locale): Long {
//        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", locale)
//
//        /// 輸出為毫秒為單位
//        return simpleDateFormat.parse(date).time
//    }
//
//    fun convertDateToTimeStamp(date: String): Long{
//        val stamp = SimpleDateFormat("yyyy-MM-dd").parse(date)
//        return stamp.time
//    }

    fun lessThenTenPadStart(time: Long): String{
        return if(time < 10){
            time.toString().padStart(2,'0')
        }else time.toString()
    }

    fun getString(resourceId: Int): String {
        return PhoneAuctionApplication.instance.getString(resourceId)
    }


}