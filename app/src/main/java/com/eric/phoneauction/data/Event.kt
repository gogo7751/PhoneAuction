package com.eric.phoneauction.data

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    var id: String = "",
    var productName: String = "",
    var buyerId: String = "",
    var sellerId: String = "",
    @get:PropertyName("isDealDone") @set:PropertyName("isDealDone")
    var isDealDone: Boolean? = null,
    var storage: String = "",
    var brand: String = "",
    var price: Int = 0,
    var images: List<String> = listOf(),
    var trade: String = "",
    var description: String = "",
    var endTime: Long = -1,
    var createdTime: Long = -1,
    var tag: String = "",
    var userId: String = "",
    var buyUser: String = "",
    var sellerImage: String = "",
    var sellerName:String = "",
    var deal: Boolean = true
): Parcelable

