package com.eric.phoneauction.data

data class Message(
    var id: String = "",
    var senderImage: String = "",
    var time: Long = -1,
    var text: String? = null,
    var image: String? = ""
) {
}