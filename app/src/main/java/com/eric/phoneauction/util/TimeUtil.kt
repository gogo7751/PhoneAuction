package com.eric.phoneauction.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun StampToDate(time: Long): String {
        // 進來的time以秒為單位，Date輸入為毫秒為單位，要注意

        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")

        return simpleDateFormat.format(Date(time))
    }

}