package com.eric.phoneauction.data.remote

import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.*
import com.eric.phoneauction.data.Collection
import com.eric.phoneauction.data.source.PhoneAuctionDataSource
import com.eric.phoneauction.util.Logger
import com.eric.phoneauction.util.Util.getString
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object PhoneAuctionRemoteDataSource :
    PhoneAuctionDataSource {

    private const val PATH_EVENTS = "events"
    private const val PATH_USER = "users"
    private const val PATH_NOTIFICATION = "notifications"
    private const val PATH_CHAT_ROOM = "chatRooms"
    private const val PATH_MESSAGE = "messages"
    private const val KEY_CREATED_TIME = "createdTime"
    private const val PATH_COLLECTION = "collections"
    private const val PATH_WISH_LIST = "wishLists"
    private const val PATH_IMAGE = "images"

    override suspend fun getEvents(): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("isDealDone", true)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun getUser(): Result<User> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_USER)
            .whereEqualTo("id", UserManager.userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var user1 = UserManager.user
                    for (document in task.result!!) {
                        val user = com.eric.phoneauction.data.User(
                            id = document.getString("id") as String,
                            image = document.getString("image") as String,
                            name = document.getString("name") as String
                        )
                        user1 = user
                    }
                    continuation.resume(Result.Success(user1))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override fun getLiveEvent(isDealDone: Boolean): MutableLiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("isDealDone", isDealDone)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Event>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)
                    val event = document.toObject(Event::class.java)
                    list.add(event)
                }

                liveData.value = list
            }
        return liveData
    }

    override suspend fun getAuction(): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("tag", getString(R.string.auction_tag))
            .whereEqualTo("isDealDone", true)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun getDirect(): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("tag", getString(R.string.direct_tag))
            .whereEqualTo("isDealDone", true)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)
                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun getSortWithTag(tag: String, sort: String, query: Query.Direction): Result<List<Event>> = suspendCoroutine { continuation ->

        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("isDealDone", true)
            .whereEqualTo("tag", tag)
            .orderBy(sort, query)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)
                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun getSort(sort: String, query: Query.Direction): Result<List<Event>> = suspendCoroutine { continuation ->

        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("isDealDone", true)
            .orderBy(sort, query)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)
                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun getNotification(): Result<List<Notification>> =
        suspendCoroutine { continuation ->
            UserManager.userId?.let {
                FirebaseFirestore.getInstance()
                    .collection(PATH_USER)
                    .document(it)
                    .collection(PATH_NOTIFICATION)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val list = mutableListOf<Notification>()
                            for (document in task.result!!) {
                                Logger.d(document.id + " => " + document.data)
                                val notification = document.toObject(Notification::class.java)
                                list.add(notification)
                            }
                            continuation.resume(Result.Success(list))
                        } else {
                            task.exception?.let {
                                Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                continuation.resume(Result.Error(it))
                                return@addOnCompleteListener
                            }
                            continuation.resume(
                                Result.Fail(
                                    PhoneAuctionApplication.instance.getString(
                                        R.string.you_know_nothing
                                    )
                                )
                            )
                        }
                    }
            }
        }

    override fun getLiveNotification(): MutableLiveData<List<Notification>> {
        val liveData = MutableLiveData<List<Notification>>()

        UserManager.userId?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_USER)
                .document(it)
                .collection(PATH_NOTIFICATION)
                .whereEqualTo("visibility", true)
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->

                    Logger.i("addSnapshotListener detect")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<Notification>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)
                        val notification = document.toObject(Notification::class.java)
                        list.add(notification)
                    }

                    liveData.value = list
                }
        }
        return liveData
    }

    override fun getLiveChatRoom(): MutableLiveData<List<ChatRoom>> {
        val liveData = MutableLiveData<List<ChatRoom>>()
        FirebaseFirestore.getInstance()
            .collection(PATH_CHAT_ROOM)
            .whereEqualTo("visibility", true)
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<ChatRoom>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)
                    val chatRoom = document.toObject(ChatRoom::class.java)
                    list.add(chatRoom)
                }

                liveData.value = list
            }
        return liveData
    }

    override fun getLiveMessage(documentId: String): MutableLiveData<List<Message>>  {
        val liveData = MutableLiveData<List<Message>>()
        FirebaseFirestore.getInstance()
            .collection(PATH_CHAT_ROOM)
            .document(documentId)
            .collection(PATH_MESSAGE)
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Message>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)
                    val chatRoom = document.toObject(Message::class.java)
                    list.add(chatRoom)
                }

                liveData.value = list
            }
        return liveData
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun post(event: Event): Result<Boolean> = suspendCoroutine { continuation ->
        val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS)
        val document = events.document(event.id)

        document
            .set(event)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("PhoneAuction: $event")
                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun postAuction(event: Event, price: Int): Result<Boolean> =
        suspendCoroutine { continuation ->
            val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS)

            events
                .document(event.id)
                .update("price", price, "buyerId", UserManager.userId)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("PhoneAuction: $event")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }

    override suspend fun postDirect(event: Event): Result<Boolean> =
        suspendCoroutine { continuation ->
            val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS).document(event.id)

            events
                .update("isDealDone", false, "buyerId", UserManager.userId)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("PhoneAuction: $event")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }


        }


    override suspend fun finishAuction(event: Event): Result<Boolean>  =

        suspendCoroutine { continuation ->
            val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS)

            val document = events.document(event.id)

            document
                .update("isDealDone", false)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("PhoneAuction: $event")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }

    override suspend fun postUser(user: User): Result<Boolean> = suspendCoroutine { continuation ->
        val users = FirebaseFirestore.getInstance().collection(PATH_USER)
        val document = UserManager.userId?.let { users.document(it) }

        user.id = UserManager.user.id
        user.image = UserManager.user.image
        user.name = UserManager.user.name

        users
            .whereEqualTo("id", UserManager.userId)
            .get()
            .addOnSuccessListener { it ->
                if (it.isEmpty) {
                    if (document != null) {
                        document
                            .set(user)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    continuation.resume(Result.Success(true))
                                } else {
                                    task.exception?.let {
                                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                        continuation.resume(Result.Error(it))
                                        return@addOnCompleteListener
                                    }
                                    continuation.resume(
                                        Result.Fail(
                                            PhoneAuctionApplication.instance.getString(
                                                R.string.you_know_nothing
                                            )
                                        )
                                    )
                                }
                            }
                    }
                } else {
                    Logger.d("已是會員")
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun postNotification(
        notification: Notification,
        buyerId: String
    ): Result<Boolean> = suspendCoroutine { continuation ->

        val notifications =
            FirebaseFirestore.getInstance().collection(PATH_USER).document(buyerId).collection(PATH_NOTIFICATION)
        val document = notifications.document()


//        notification.event?.buyerId = UserManager.userId.toString()
        notification.id = document.id
        notification.time = Calendar.getInstance().timeInMillis

        document
            .set(notification)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("PhoneAuction: $notification")
                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun deleteNotification(notificationId: String, user: String): Result<Boolean> =
        suspendCoroutine { continuation ->

            val notifications =
                UserManager.userId?.let {
                    FirebaseFirestore.getInstance().collection(PATH_USER).document(it).collection(
                        PATH_NOTIFICATION
                    )
                }
            val document = notifications?.document(notificationId)

            document
                ?.update("visibility", false)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("PhoneAuction: $notificationId")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }



    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun postChatRoom(chatRoom: ChatRoom): Result<Boolean> =
        suspendCoroutine { continuation ->

            val chatRooms = FirebaseFirestore.getInstance().collection(PATH_CHAT_ROOM)
            val document = chatRooms.document(chatRoom.id)

            chatRoom.time = Calendar.getInstance().timeInMillis

            chatRooms
                .whereEqualTo("id", chatRoom.id)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        document
                            .set(chatRoom)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Logger.i("PhoneAuction: $chatRoom")
                                    continuation.resume(Result.Success(true))
                                } else {
                                    task.exception?.let {
                                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                        continuation.resume(Result.Error(it))
                                        return@addOnCompleteListener
                                    }
                                    continuation.resume(
                                        Result.Fail(
                                            PhoneAuctionApplication.instance.getString(
                                                R.string.you_know_nothing
                                            )
                                        )
                                    )
                                }
                            }
                    } else {
                        Logger.d("聊天室已存在")
                    }
                }
        }



    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun postMessage(message: Message, document: String): Result<Boolean> =
        suspendCoroutine { continuation ->

            val messages =
                FirebaseFirestore.getInstance().collection(PATH_CHAT_ROOM).document(document)
                    .collection(PATH_MESSAGE)
            val updateMessages = FirebaseFirestore.getInstance().collection(PATH_CHAT_ROOM).document(document)
            val document = messages.document()

            message.time = Calendar.getInstance().timeInMillis

            updateMessages
                .update("text", message.text , "time", message.time)

            document
                .set(message)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("PhoneAuction: $message")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(
                                PhoneAuctionApplication.instance.getString(
                                    R.string.you_know_nothing
                                )
                            )
                        )
                    }
                }
        }

    override suspend fun deleteChatRoom(chatRoomId: String): Result<Boolean>  =
        suspendCoroutine { continuation ->

            val chatRooms = FirebaseFirestore.getInstance().collection(PATH_CHAT_ROOM)

            val document = chatRooms.document(chatRoomId)

            document
                .update("visibility", false)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("PhoneAuction: $chatRoomId")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }

    override suspend fun postCollection(collection: Collection, user: User): Result<Boolean> =
        suspendCoroutine { continuation ->

            val collections =
                FirebaseFirestore.getInstance().collection(PATH_USER).document(user.id)
                    .collection(PATH_COLLECTION)
            val document = collection.event?.id?.let { collections.document(it) }

            collection.id = collection.event?.id.toString()

            document
                ?.set(collection)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("PhoneAuction: $collection")
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(
                                PhoneAuctionApplication.instance.getString(
                                    R.string.you_know_nothing
                                )
                            )
                        )
                    }
                }
        }

    override suspend fun getCollection(id: String): Result<Collection> = suspendCoroutine { continuation ->
        UserManager.userId?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_USER)
                .document(it)
                .collection(PATH_COLLECTION)
                .whereEqualTo("id", id)
                .whereEqualTo("visibility", true)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var getCollection = Collection()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val collection = document.toObject(Collection::class.java)
                            getCollection = collection
                        }
                        continuation.resume(Result.Success(getCollection))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }
    }

    override suspend fun getAllCollection(): Result<List<Collection>> = suspendCoroutine { continuation ->
        UserManager.userId?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_USER)
                .document(it)
                .collection(PATH_COLLECTION)
                .whereEqualTo("visibility", true)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var list = mutableListOf<Collection>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)

                            val collection = document.toObject(Collection::class.java)
                            list.add(collection)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {

                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }
    }

    override fun getAllLiveCollection(): MutableLiveData<List<Collection>> {
        val liveData = MutableLiveData<List<Collection>>()

        UserManager.userId?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_USER)
                .document(it)
                .collection(PATH_COLLECTION)
                .whereEqualTo("visibility", true)
                .addSnapshotListener { snapshot, exception ->

                    Logger.i("addSnapshotListener detect")

                    exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                    }

                    val list = mutableListOf<Collection>()
                    for (document in snapshot!!) {
                        Logger.d(document.id + " => " + document.data)
                        val collection = document.toObject(Collection::class.java)
                        list.add(collection)
                    }

                    liveData.value = list
                }
        }
        return liveData
    }

    override fun getLiveSearch(field: String, searchKey: String): MutableLiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("isDealDone", true)
            .orderBy(field)
            .startAt(searchKey.toUpperCase())
            .endAt(searchKey.toLowerCase()+"\uf8ff")
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Event>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)
                    val event = document.toObject(Event::class.java)
                    list.add(event)
                }

                liveData.value = list
            }
        return liveData
    }

    override suspend fun getAveragePrice(brand: String, productName: String, storage: String, isDealDone: Boolean): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("brand", brand)
            .whereEqualTo("productName", productName)
            .whereEqualTo("storage", storage)
            .whereEqualTo("isDealDone", isDealDone)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Event>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)

                        val event = document.toObject(Event::class.java)
                        list.add(event)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun postWishList(wishList: WishList): Result<Boolean>  = suspendCoroutine { continuation ->
        val wishLists = FirebaseFirestore.getInstance().collection(PATH_WISH_LIST)
        val document = wishLists.document()

        wishList.id = document.id

        document
            .set(wishList)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("PhoneAuction: $wishList")
                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override fun getWishList(): MutableLiveData<List<WishList>> {
        val liveData = MutableLiveData<List<WishList>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_WISH_LIST)
            .whereEqualTo("userId", UserManager.userId)
            .whereEqualTo("visibility", true)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<WishList>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)
                    val wishList = document.toObject(WishList::class.java)
                    list.add(wishList)
                }

                liveData.value = list
            }
        return liveData
    }

    override suspend fun getWishListFromPost(brand: String, productName: String, storage: String, visibility: Boolean):
            Result<WishList> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_WISH_LIST)
            .whereEqualTo("brand", brand)
            .whereEqualTo("productName", productName)
            .whereEqualTo("storage", storage)
            .whereEqualTo("visibility", visibility)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var list = WishList()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)

                        val wishList = document.toObject(WishList::class.java)
                        list = wishList
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun handleFacebookAccessToken(token: AccessToken?): Result<Boolean> = suspendCoroutine { continuation ->
        val auth = FirebaseAuth.getInstance()
        val credential = token?.token?.let { FacebookAuthProvider.getCredential(it) }
        Logger.d("${token!!.token}")
        if (credential != null) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener {
                        task ->
                    if (task.isSuccessful) {
                        val user = User(
                            id = task.result?.user?.uid.toString(),
                            image = task.result?.user?.photoUrl.toString(),
                            name = task.result?.user?.displayName.toString()
                        )
                        UserManager.userId = task.result?.user?.uid.toString()
                        UserManager.user = user
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                    }
                }
        }
    }

    override suspend fun updateWishList(id: String): Result<Boolean> = suspendCoroutine { continuation ->
        val wishLists = FirebaseFirestore.getInstance().collection(PATH_WISH_LIST)
        val document = wishLists.document(id)

        document
            .update("visibility", false)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("PhoneAuction: $id")
                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(PhoneAuctionApplication.instance.getString(R.string.you_know_nothing)))
                }
            }
    }

    override suspend fun uploadImage(image: MutableLiveData<String>, saveUri: Uri): Result<Boolean> = suspendCoroutine { continuation ->
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/$PATH_IMAGE/$filename")
        saveUri.let { uri ->
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        image.value = it.toString()
                        continuation.resume(Result.Success(true))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Result.Error(it))
                    return@addOnFailureListener
                }
        }
    }
}




