package com.eric.phoneauction.data

data class ChatRoom(
    val id: String = "",
    val text: String = "",
    val time: Long = -1,
    val senderImage: String = "",
    val senderId: String = "",
    var event: Event? = Event()
) {
}