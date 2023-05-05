package com.application.vladcelona.eximeeting.firebase

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

private const val TAG = "EximeetingFirebase"

class EximeetingFirebase {

    companion object {

        fun getUserData(): User {
            var user: User = User()

            val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            val uid: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

            databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Error while trying to get data from Firebase")
                }
            })

            return user
        }

        fun getEvents(): ArrayList<Event?> {
            val firebaseEvents: ArrayList<Event?> = ArrayList()

            val databaseReference = FirebaseDatabase.getInstance()
                .getReference("Events").child("Events")

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        firebaseEvents.add(postSnapshot.getValue(Event::class.java))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Error while trying to get data from Firebase")
                }

            })

            return firebaseEvents
        }
    }
}