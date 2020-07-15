package com.eric.phoneauction.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notification(
    var id: String = "",
    var title: String = "",
    var time: Long = -1,
    var brand: String = "",
    var name: String = "",
    var image: String = "",
    var storage: String = "",
    var visibility: Boolean = true,
    var event: Event? = Event()
): Parcelable {
}