package com.eric.phoneauction.data

data class Collection(
    var id: String = "",
    var visibility: Boolean = false,
    var event: Event? = Event()
) {
}