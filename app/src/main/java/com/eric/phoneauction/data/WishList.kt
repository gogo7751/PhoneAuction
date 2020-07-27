package com.eric.phoneauction.data

data class WishList(
    var id: String = "",
    var brand: String = "",
    var productName: String = "",
    var storage: String = "",
    var userId: String = "",
    val visibility: Boolean = true
) {
}