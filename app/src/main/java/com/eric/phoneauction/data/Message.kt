package com.eric.phoneauction.data

data class Message(
    val id: String = "",
    val senderId: String = "",
    val recipientId: String = "",
    val time: Long = -1,
    val text: String = "",
    var photoUrl: String? = ""
) {
}