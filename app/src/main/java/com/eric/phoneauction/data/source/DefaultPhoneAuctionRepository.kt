package com.eric.phoneauction.data.source

import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.data.*
import com.eric.phoneauction.data.Collection
import com.google.firebase.firestore.Query


/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation to load Publisher sources.
 */
class DefaultPhoneAuctionRepository(private val remoteDataSource: PhoneAuctionDataSource,
                                    private val localDataSource: PhoneAuctionDataSource
) : PhoneAuctionRepository {
    override suspend fun getEvents(): Result<List<Event>> {
        return remoteDataSource.getEvents()
    }

    override fun getLiveEvent(deal: Boolean): MutableLiveData<List<Event>> {
        return remoteDataSource.getLiveEvent(deal)
    }

    override suspend fun getUser(): Result<User> {
        return remoteDataSource.getUser()
    }

    override suspend fun getAuction(): Result<List<Event>> {
        return remoteDataSource.getAuction()
    }

    override suspend fun getDirect(): Result<List<Event>> {
        return remoteDataSource.getDirect()
    }

    override suspend fun getNotification(): Result<List<Notification>> {
        return remoteDataSource.getNotification()
    }

    override fun getLiveNotification(): MutableLiveData<List<Notification>> {
        return remoteDataSource.getLiveNotification()
    }

    override fun getLiveChatRoom(): MutableLiveData<List<ChatRoom>> {
        return remoteDataSource.getLiveChatRoom()
    }

    override fun getLiveMessage(documentId: String): MutableLiveData<List<Message>> {
        return remoteDataSource.getLiveMessage(documentId)
    }

    override suspend fun getSortWithTag(tag: String, sort: String, query: Query.Direction): Result<List<Event>> {
        return remoteDataSource.getSortWithTag(tag, sort, query)
    }

    override suspend fun getSort(sort: String, query: Query.Direction): Result<List<Event>> {
        return remoteDataSource.getSort(sort, query)
    }

    override suspend fun post(event: Event): Result<Boolean> {
        return remoteDataSource.post(event)
    }

    override suspend fun postAuction(event: Event, price: Int): Result<Boolean> {
        return remoteDataSource.postAuction(event, price)
    }

    override suspend fun postDirect(event: Event): Result<Boolean> {
        return remoteDataSource.postDirect(event)
    }

    override suspend fun postNotification(notification: Notification, buyUser: String): Result<Boolean> {
        return remoteDataSource.postNotification(notification, buyUser)
    }

    override suspend fun deleteNotification(notificationId: String, user: String): Result<Boolean> {
        return remoteDataSource.deleteNotification(notificationId, user)
    }

    override suspend fun postUser(user: User): Result<Boolean> {
        return remoteDataSource.postUser(user)
    }

    override suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean> {
        return remoteDataSource.postChatRoom(chatRoom)
    }

    override suspend fun postMessage(message: Message, document: String): Result<Boolean> {
        return remoteDataSource.postMessage(message, document)
    }

    override suspend fun deleteChatRoom(chatRoomId: String): Result<Boolean> {
        return remoteDataSource.deleteChatRoom(chatRoomId)
    }

    override suspend fun finishAuction(event: Event): Result<Boolean> {
        return remoteDataSource.finishAuction(event)
    }

    override suspend fun postCollection(collection: Collection, user: User): Result<Boolean> {
        return  remoteDataSource.postCollection(collection, user)
    }

    override suspend fun getCollection(id: String): Result<Collection> {
        return remoteDataSource.getCollection(id)
    }

    override suspend fun getAllCollection(): Result<List<Collection>> {
        return remoteDataSource.getAllCollection()
    }

    override fun getAllLiveCollection(): MutableLiveData<List<Collection>> {
        return remoteDataSource.getAllLiveCollection()
    }

    override fun getLiveSearch(field: String, searchKey: String): MutableLiveData<List<Event>> {
        return remoteDataSource.getLiveSearch(field, searchKey)
    }

    override suspend fun getAveragePrice(brand: String, productName: String, storage: String, deal: Boolean
    ): Result<List<Event>> {
        return remoteDataSource.getAveragePrice(brand, productName, storage, deal)
    }
}
