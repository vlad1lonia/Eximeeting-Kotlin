package com.application.vladcelona.eximeeting.firebase

import android.util.Log
import com.application.vladcelona.eximeeting.data_classes.event.Event
import com.application.vladcelona.eximeeting.data_classes.event.FirebaseEvent
import com.application.vladcelona.eximeeting.data_classes.user.FirebaseUser
import com.application.vladcelona.eximeeting.data_classes.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
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

            Log.d(TAG, objectId.toString())

            val objectReference = Firebase.firestore.collection(collection)
                .document(objectId.toString())

            Log.d(TAG, getCollection("users").toString())

            return when (collection) {

                "users" -> {
                    var documentUser = FirebaseUser()

                    objectReference.get().addOnSuccessListener { documentSnapshot ->
                        documentUser = documentSnapshot.toObject<FirebaseUser>()!!
                    }

                    Log.d(TAG, documentUser.toString())
                    documentUser.convert()
                }

                "events" -> {
                    var documentEvent = FirebaseEvent()

                    objectReference.get().addOnSuccessListener { documentSnapshot ->
                        documentEvent = documentSnapshot.toObject<FirebaseEvent>()!!
                    }

                    Log.d(TAG, documentEvent.toString())
                    documentEvent.convert()
                }

                else -> {
                    Log.d(TAG, "Wrong collection parameter input")
                    Any()
                }
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
        fun getCollection(collection: String): ArrayList<Any> {

            var collectionData: ArrayList<Any> = ArrayList()
            getDataOnCallback(collection) { firestoreList ->
                collectionData = firestoreList as ArrayList<Any>
            }

            return collectionData
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

        /**
         * Method for getting all document snapshots from Cloud Firestore
         * @author Balandin (Vladcelona) Vladislav
         */
        private fun getDataOnCallback(collection: String, dataCallback: (List<Any>) -> Unit) {

            val firestoreReference = Firebase.firestore.collection(collection).get()

            when (collection) {

                "users" -> {
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

                "events" -> {
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
            }
        }
    }
}