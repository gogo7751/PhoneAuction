package com.eric.phoneauction.data.remote

import android.icu.util.Calendar
import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.*
import com.eric.phoneauction.data.source.PhoneAuctionDataSource
import com.eric.phoneauction.util.Logger
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    override suspend fun getEvents(): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("deal", true)
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

    override fun getLiveEvent(): MutableLiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
            .whereEqualTo("deal", true)
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
            .whereEqualTo("tag", "拍賣")
            .whereEqualTo("deal", true)
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
            .whereEqualTo("tag", "直購")
            .whereEqualTo("deal", true)
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

    override suspend fun post(event: Event): Result<Boolean> = suspendCoroutine { continuation ->
        val events = FirebaseFirestore.getInstance().collection(PATH_EVENTS)
        val document = events.document()

        event.id = document.id
        event.createdTime = Calendar.getInstance().timeInMillis
        event.endTime = Calendar.getInstance().timeInMillis + 259200

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
                .update("price", price, "buyUser", UserManager.userId)
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
                .update("deal", false, "buyUser", UserManager.userId)
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

    override suspend fun postNotification(
        notification: Notification,
        buyUser: String
    ): Result<Boolean> = suspendCoroutine { continuation ->

        val notifications =
            FirebaseFirestore.getInstance().collection(PATH_USER).document(buyUser).collection(
                PATH_NOTIFICATION
            )
        val document = notifications.document()

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

    override suspend fun postMessage(message: Message, document: String): Result<Boolean> =
        suspendCoroutine { continuation ->

            val messages =
                FirebaseFirestore.getInstance().collection(PATH_CHAT_ROOM).document(document)
                    .collection(PATH_MESSAGE)
            val document = messages.document()

            message.time = Calendar.getInstance().timeInMillis

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
}




