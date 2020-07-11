package com.eric.phoneauction.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var image: String = "",
    var name: String = ""
): Parcelable