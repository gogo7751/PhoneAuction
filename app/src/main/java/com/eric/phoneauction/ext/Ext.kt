package com.eric.phoneauction.ext

import android.icu.text.SimpleDateFormat
import java.util.*

fun Long.toDisplayFormat(): String {
    return SimpleDateFormat("HH:mm:ss", Locale.TAIWAN).format(this)
}