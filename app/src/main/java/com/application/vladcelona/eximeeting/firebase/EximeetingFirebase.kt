package com.application.vladcelona.eximeeting.firebase

import android.util.Log
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlin.random.Random

private const val TAG = "EximeetingFirebase"

// TODO: Change Realtime Database for Firestore
class EximeetingFirebase {

    companion object {

        /**
         * This is a test method, to check teh correct data upload to Firestore
         */
        fun testFirestore() {

            val firestore = Firebase.firestore
            val testUser: User = User(
                fullName = "Balandin Vladislav",
                email = "vladbalandin2013@gmail.com",
                companyName = "Eximeeting",
                birthDate = "02/11/2004"
            )

            firestore.collection("users")
                .document(testUser.id.toString()).set(testUser)
                .addOnSuccessListener { Log.d(TAG, "Snapshot successfully written!") }
                .addOnFailureListener { e -> Log.d(TAG, "Failed to write document", e) }
        }

        // TODO: Change Realtime Database for Firestore
        fun getEventData(): List<Event> {

            val events: ArrayList<Event> = ArrayList()

            val databaseReference: DatabaseReference = FirebaseDatabase
                .getInstance().getReference("Events")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (eventSnapshot in snapshot.children) {
                        val jsonEvent: String? = eventSnapshot.getValue(String::class.java)
                        if (jsonEvent != null) {
                            events.add(Gson().fromJson(jsonEvent, Event::class.java))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "The load of Events has been cancelled")
                }

            })

            val eventsId: HashMap<String, Boolean> = HashMap()
            for (event in events) {
                eventsId[event.id.toString()] = false
            }

            Log.i(TAG, Gson().toJson(eventsId))

            return events
        }
    }
}