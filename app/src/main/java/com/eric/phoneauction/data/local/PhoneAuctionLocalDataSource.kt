package com.eric.phoneauction.data.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.data.*
import com.eric.phoneauction.data.Collection
import com.eric.phoneauction.data.source.PhoneAuctionDataSource
import com.facebook.AccessToken
import com.google.firebase.firestore.Query

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation of a Publisher source as a db.
 */
class PhoneAuctionLocalDataSource(val context: Context) :
    PhoneAuctionDataSource {

    override suspend fun getEvents(): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getLiveEvent(deal: Boolean): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun post(event: Event): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotification(): Result<List<Notification>> {
        TODO("Not yet implemented")
    }

    override fun getLiveNotification(): MutableLiveData<List<Notification>> {
        TODO("Not yet implemented")
    }

    override fun getLiveChatRoom(): MutableLiveData<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override fun getLiveMessage(documentId: String): MutableLiveData<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSortWithTag(tag: String, sort: String, query: Query.Direction): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSort(sort: String, query: Query.Direction): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun postDirect(event: Event): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postNotification(notification: Notification, buyUser: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotification(notificationId: String, user: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postAuction(event: Event, price: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAuction(): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDirect(): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun postMessage(message: Message, document: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChatRoom(chatRoomId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun finishAuction(event: Event): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postCollection(collection: Collection, user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollection(id: String): Result<Collection> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCollection(): Result<List<Collection>> {
        TODO("Not yet implemented")
    }

    override fun getAllLiveCollection(): MutableLiveData<List<Collection>> {
        TODO("Not yet implemented")
    }

    override fun getLiveSearch(field: String, searchKey: String): MutableLiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAveragePrice(brand: String, productName: String, storage: String, deal: Boolean
    ): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun postWishList(wishList: WishList): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getWishList(): MutableLiveData<List<WishList>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateWishList(id: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getWishListFromPost(brand: String, productName: String, storage: String, visibility: Boolean
    ): Result<WishList> {
        TODO("Not yet implemented")
    }

    override suspend fun handleFacebookAccessToken(token: AccessToken?): Result<Boolean> {
        TODO("Not yet implemented")
    }
}
