package com.application.vladcelona.eximeeting.firebase

import android.util.Log
import com.application.vladcelona.eximeeting.data_classes.BusinessCard
import com.application.vladcelona.eximeeting.data_classes.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG = "EximeetingFirebase"

class EximeetingFirebase {

    companion object {

        fun getEvents(): List<Event> {
            return emptyList()
        }

        fun getBusinessCard(): BusinessCard? {
            var businessCard: BusinessCard? = null

            val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

            databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    businessCard = snapshot.getValue(BusinessCard::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to upload information from database")
                }
            })

            return businessCard
        }
    }
}