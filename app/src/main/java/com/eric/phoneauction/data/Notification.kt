package com.eric.phoneauction.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notification(
    var title: String = "",
    var time: Long = -1,
    var visibility: Boolean = true
): Parcelable {
}