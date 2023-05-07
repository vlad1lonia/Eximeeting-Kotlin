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
import kotlin.random.Random

private const val TAG = "EximeetingFirebase"

class EximeetingFirebase {

    companion object {
        
        fun pushEventData() {
            val descriptionText = """Join us for an exciting evening of networking and knowledge sharing at our annual Business Innovation Summit. This event brings together industry leaders, entrepreneurs, and professionals from diverse sectors to explore the latest trends and strategies shaping the business landscape.

Featuring renowned keynote speakers, interactive panel discussions, and breakout sessions, the Business Innovation Summit offers valuable insights into disruptive technologies, market trends, and successful business models. Gain inspiration from thought-provoking presentations and engage in meaningful conversations with like-minded professionals.

Whether you're an aspiring entrepreneur, a seasoned executive, or a business enthusiast, this event provides an excellent platform to expand your network, forge new partnerships, and stay ahead of the curve. Connect with industry experts, discover innovative solutions, and explore opportunities for collaboration in a dynamic and vibrant environment.

Don't miss this opportunity to join the conversation and be part of the business revolution. Reserve your spot at the Business Innovation Summit today and unlock new possibilities for growth, innovation, and success."""

            val events: ArrayList<FirebaseEvent> = ArrayList()

            var event = Event(
                Random.nextInt(), "First Conference", Event.randomDate(),
                Event.randomDate(), "Moscow, Russia", "117892, Random Street",
                "Eximeeting", descriptionText).convertToFirebase()
            events.add(event)
            event = Event(
                Random.nextInt(), "Second Conference", Event.randomDate(),
                Event.randomDate(), "Washington D.C., USA", "Random Square",
                "Eximeeting", descriptionText).convertToFirebase()
            events.add(event)
            event = Event(
                Random.nextInt(), "Third Conference", Event.randomDate(),
                Event.randomDate(), "Madrid, Spain", "Random Square",
                "Eximeeting", descriptionText).convertToFirebase()
            events.add(event)
            event = Event(
                Random.nextInt(), "Fourth Conference", Event.randomDate(),
                Event.randomDate(), "Casablanca, Morocco", "Random Square",
                "Eximeeting", descriptionText).convertToFirebase()
            events.add(event)
            event = Event(
                Random.nextInt(), "Fifth Conference", Event.randomDate(),
                Event.randomDate(), "Minsk, Belarus", "Random Drive",
                "Eximeeting", descriptionText).convertToFirebase()
            events.add(event)
            event = Event(
                Random.nextInt(), "Sixth Conference", Event.randomDate(),
                Event.randomDate(), "Los Angeles, CA, USA", "Random Roas",
                "Eximeeting", descriptionText).convertToFirebase()
            events.add(event)
            event = Event(
                Random.nextInt(), "Seventh Conference", Event.randomDate(),
                Event.randomDate(), "Washington D.C., USA", "Random Event",
                "Eximeeting", descriptionText).convertToFirebase()
            events.add(event)
            event = Event(
                Random.nextInt(), "Eighth Conference", Event.randomDate(),
                Event.randomDate(), "Seattle, WA, USA", "Random Street",
                "Eximeeting", descriptionText).convertToFirebase()
            events.add(event)

            val databaseReference = FirebaseDatabase.getInstance().getReference("Events")
            for (element in events) {
                databaseReference.child(element.id.toString()).setValue(element)
            }
        }

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
    }
}