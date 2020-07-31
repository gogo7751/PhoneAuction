package com.eric.phoneauction.ext

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
fun Long.toDisplayFormat(): String {
    return SimpleDateFormat("HH:mm", Locale.TAIWAN).format(this)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}