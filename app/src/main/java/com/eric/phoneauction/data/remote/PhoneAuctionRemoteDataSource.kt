package com.eric.phoneauction.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.eric.phoneauction.PhoneAuctionApplication
import com.eric.phoneauction.R
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.data.Result
import com.eric.phoneauction.data.source.PhoneAuctionDataSource
import com.eric.phoneauction.util.Logger
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object PhoneAuctionRemoteDataSource :
    PhoneAuctionDataSource {

    private const val PATH_EVENTS = "events"
    private const val KEY_CREATED_TIME = "createdTime"

    override suspend fun getEvents(): Result<List<Event>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_EVENTS)
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
                    Log.d("123con", "${continuation.resume(Result.Success(list))}")
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
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("addSnapshotListener detect")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Event>()
                for (document in snapshot!!) {
                    Logger.d(document.id + " => " + document.data)

                    val article = document.toObject(Event::class.java)
                    list.add(article)
                }

                liveData.value = list
            }
        return liveData
    }
}
