package com.eric.phoneauction.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val id: String = "",
    val productName: String = "",
    val brand: String = "",
    val price: Int = -1,
    val images: String = "",
    val trade: String = "",
    val description: String = "",
    val endTime: Long = -1,
    val createdTime: Long = -1,
    val tag: String = "",
    val userId: String = ""
): Parcelable