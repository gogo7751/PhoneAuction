package com.eric.phoneauction.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoom(
    var id: String = "",
    var text: String = "",
    var time: Long = -1,
    var senderImage: String = "",
    var senderId: String = "",
    var senderName: String = "",
    var receiverId: String = "",
    var receiverImage: String = "",
    var receiverName: String = "",
    var productImage:String = "",
    var visibility: Boolean = true,
    var event: Event? = Event()
): Parcelable {
}