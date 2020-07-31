package com.eric.phoneauction.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.data.*
import com.eric.phoneauction.data.Collection
import com.facebook.AccessToken
import com.google.firebase.firestore.Query


/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Interface to the Publisher layers.
 */
interface PhoneAuctionRepository {

    suspend fun getEvents(): Result<List<Event>>

    fun getLiveEvent(deal: Boolean): MutableLiveData<List<Event>>

    suspend fun getAuction(): Result<List<Event>>

    suspend fun getDirect(): Result<List<Event>>

    suspend fun getSortWithTag(tag: String, sort: String, query: Query.Direction): Result<List<Event>>

    suspend fun getSort(sort: String, query: Query.Direction): Result<List<Event>>

    suspend fun getUser(): Result<User>

    suspend fun getNotification(): Result<List<Notification>>

    fun getLiveNotification(): MutableLiveData<List<Notification>>

    fun getLiveChatRoom(): MutableLiveData<List<ChatRoom>>

    fun getLiveMessage(documentId: String): MutableLiveData<List<Message>>

    suspend fun post(event: Event): Result<Boolean>

    suspend fun postUser(user: User): Result<Boolean>

    suspend fun postAuction(event: Event, price: Int): Result<Boolean>

    suspend fun postDirect(event: Event): Result<Boolean>

    suspend fun postNotification(notification: Notification, buyUser: String): Result<Boolean>

    suspend fun deleteNotification(notificationId: String, user: String): Result<Boolean>

    suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean>

    suspend fun postMessage(message: Message, document: String): Result<Boolean>

    suspend fun deleteChatRoom(chatRoomId: String): Result<Boolean>

    suspend fun finishAuction(event: Event): Result<Boolean>

    suspend fun postCollection(collection: Collection, user: User): Result<Boolean>

    suspend fun getCollection(id: String): Result<Collection>

    suspend fun getAllCollection(): Result<List<Collection>>

    fun getAllLiveCollection(): MutableLiveData<List<Collection>>

    fun getLiveSearch(field: String, searchKey: String): MutableLiveData<List<Event>>

    suspend fun getAveragePrice(brand: String, productName: String, storage: String, deal: Boolean): Result<List<Event>>

    suspend fun postWishList(wishList: WishList): Result<Boolean>

    suspend fun updateWishList(id: String): Result<Boolean>

    fun getWishList(): MutableLiveData<List<WishList>>

    suspend fun getWishListFromPost(brand: String, productName: String, storage: String, visibility: Boolean): Result<WishList>

    suspend fun handleFacebookAccessToken(token : AccessToken?): Result<Boolean>
}
