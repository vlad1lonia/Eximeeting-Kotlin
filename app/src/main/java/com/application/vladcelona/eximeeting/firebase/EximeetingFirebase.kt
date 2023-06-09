package com.application.vladcelona.eximeeting.firebase

import android.util.Log
import com.application.vladcelona.eximeeting.data_classes.event.Event
import com.application.vladcelona.eximeeting.data_classes.event.FirebaseEvent
import com.application.vladcelona.eximeeting.data_classes.user.FirebaseUser
import com.application.vladcelona.eximeeting.data_classes.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "EximeetingFirebase"

class EximeetingFirebase {

    companion object {

        /**
         * Method for pushing type [Any] [objectData] object to Cloud Firestore
         * to collection [collection], the given object type has to be either [User] or [Event].
         * Otherwise the function logs an error
         * @author Balandin (Vladcelona) Vladislav
         */
        fun pushDocument(collection: String, objectData: Any) {

            val firestore = Firebase.firestore

            when (objectData) {

                is User -> {
                    val convertedUser: FirebaseUser = objectData.convert()

                    firestore.collection(collection)
                        .document(convertedUser.id).set(convertedUser.toMap())
                        .addOnSuccessListener {
                            Log.d(TAG, "Snapshot successfully written")
                        }
                        .addOnFailureListener { Log.d(TAG, "Failed to write snapshot") }
                }

                is Event -> {
                    val convertedEvent: FirebaseEvent = objectData.convert()

                    firestore.collection(collection)
                        .document(convertedEvent.id).set(convertedEvent.toMap())
                        .addOnSuccessListener {
                            Log.d(TAG, "Snapshot successfully written")
                        }
                        .addOnFailureListener { Log.d(TAG, "Failed to write snapshot") }
                }

                else -> {
                    Log.w(TAG, "Wrong data input ot the function")
                }
            }
        }

        /**
         * Method for getting document data with ID [objectId] from collection [collection]
         * and converting the received document to type corresponding with the [collection] string
         * @return A new instance of [Any]
         * @author Balandin (Vladcelona) Vladislav
         */
        fun getDocument(collection: String, objectId: Any): Any {

            val collectionData: ArrayList<Any> = getCollection(collection) as ArrayList<Any>
            when (collection) {

                "users" -> {
                    for (document in collectionData) {
                        val userDocument = document as FirebaseUser
                        if (userDocument.id == objectId.toString()) {
                            return userDocument
                        }
                    }

                    return FirebaseUser()
                }

                "events" -> {
                    for (document in collectionData) {
                        val eventDocument = document as FirebaseEvent
                        if (eventDocument.id == objectId.toString()) {
                            return eventDocument
                        }
                    }

                    return FirebaseEvent()
                }

                else -> { return Any() }
            }
        }

        /**
         * Method for updating document data of [objectData] from collection [collection]
         * @author Balandin (Vladcelona) Vladislav
         */
        fun updateDocument(collection: String, objectData: Any) {

            val firestore = Firebase.firestore

            when (objectData) {

                is User -> {
                    val convertedUser: FirebaseUser = objectData.convert()
                    firestore.collection(collection)
                        .document(convertedUser.id).set(convertedUser)
                }

                is Event -> {
                    val convertedEvent: FirebaseEvent = objectData.convert()
                    firestore.collection(collection)
                        .document(convertedEvent.id).set(convertedEvent)
                }
            }
        }

        /**
         * Method for getting all the documents from a given [collection]
         * @return A new instance of [ArrayList]
         * @author Balandin (Vladcelona) Vladislav
         */
        fun getCollection(collection: String): Any {

            when (collection) {

                "users" -> {
                    val collectionUsers: ArrayList<FirebaseUser> = ArrayList()

                    getUsersOnCallback { firestoreList ->
                        for (document in firestoreList) {
                            collectionUsers.add(document as FirebaseUser)
                        }
                    }

                    return collectionUsers
                }

                "events" -> {
                    val collectionEvents: ArrayList<FirebaseEvent> = ArrayList()

                    getEventsOnCallback { firestoreList ->
                        for (document in firestoreList) {
                            collectionEvents.add(document as FirebaseEvent)
                        }
                    }

                    return collectionEvents
                }

                else -> { return arrayListOf<Any>() }
            }
        }

        /**
         * Method for getting all document snapshots from "users" collection in Cloud Firestore
         * @author Balandin (Vladcelona) Vladislav
         */
        private fun getUsersOnCallback(dataCallback: (List<Any>) -> Unit) {

            val firestoreReference = Firebase.firestore.collection("users").get()

            firestoreReference.addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful) {

                    val list = ArrayList<FirebaseUser>()
                    for (document in snapshot.result) {
                        list.add(document.toObject(FirebaseUser::class.java))
                    }

                    dataCallback(list)
                }
            }
        }

        /**
         * Method for getting all document snapshots from "events" collection in Cloud Firestore
         * @author Balandin (Vladcelona) Vladislav
         */
        private fun getEventsOnCallback(dataCallback: (List<Any>) -> Unit) {

            val firestoreReference = Firebase.firestore.collection("events").get()

            firestoreReference.addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful) {

                    val list = ArrayList<FirebaseEvent>()
                    for (document in snapshot.result) {
                        list.add(document.toObject(FirebaseEvent::class.java))
                    }

                    dataCallback(list)
                }
            }
        }

        /**
         * Method for checking if current application user has an account in Firebase
         * @return A new instance of Boolean
         * @author Balandin (Vladcelona) Vladislav
         */
        fun checkUserAccess(): Boolean {

            return when (FirebaseAuth.getInstance().currentUser) {
                null -> false
                else -> true
            }
        }
    }
}