package com.application.vladcelona.eximeeting.firebase

import android.util.Log
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlin.random.Random

private const val TAG = "EximeetingFirebase"

class EximeetingFirebase {

    companion object {
        
        fun pushEventData() {
            val descriptionText = """Join us for an exciting evening of networking and knowledge sharing at our annual Business Innovation Summit. This event brings together industry leaders, entrepreneurs, and professionals from diverse sectors to explore the latest trends and strategies shaping the business landscape.

Featuring renowned keynote speakers, interactive panel discussions, and breakout sessions, the Business Innovation Summit offers valuable insights into disruptive technologies, market trends, and successful business models. Gain inspiration from thought-provoking presentations and engage in meaningful conversations with like-minded professionals.

Whether you're an aspiring entrepreneur, a seasoned executive, or a business enthusiast, this event provides an excellent platform to expand your network, forge new partnerships, and stay ahead of the curve. Connect with industry experts, discover innovative solutions, and explore opportunities for collaboration in a dynamic and vibrant environment.

Don't miss this opportunity to join the conversation and be part of the business revolution. Reserve your spot at the Business Innovation Summit today and unlock new possibilities for growth, innovation, and success."""

            val events: ArrayList<Event> = ArrayList()

            var event = Event(
                Random.nextInt(), "First Conference", Event.randomDate(),
                Event.randomDate(), "Moscow, Russia", "117892, Random Street",
                "Eximeeting", descriptionText)
            events.add(event)
            event = Event(
                Random.nextInt(), "Second Conference", Event.randomDate(),
                Event.randomDate(), "Washington D.C., USA", "Random Square",
                "Eximeeting", descriptionText)
            events.add(event)
            event = Event(
                Random.nextInt(), "Third Conference", Event.randomDate(),
                Event.randomDate(), "Madrid, Spain", "Random Square",
                "Eximeeting", descriptionText)
            events.add(event)
            event = Event(
                Random.nextInt(), "Fourth Conference", Event.randomDate(),
                Event.randomDate(), "Casablanca, Morocco", "Random Square",
                "Eximeeting", descriptionText)
            events.add(event)
            event = Event(
                Random.nextInt(), "Fifth Conference", Event.randomDate(),
                Event.randomDate(), "Minsk, Belarus", "Random Drive",
                "Eximeeting", descriptionText)
            events.add(event)
            event = Event(
                Random.nextInt(), "Sixth Conference", Event.randomDate(),
                Event.randomDate(), "Los Angeles, CA, USA", "Random Road",
                "Eximeeting", descriptionText)
            events.add(event)
            event = Event(
                Random.nextInt(), "Seventh Conference", Event.randomDate(),
                Event.randomDate(), "Washington D.C., USA", "Random Event",
                "Eximeeting", descriptionText)
            events.add(event)
            event = Event(
                Random.nextInt(), "Eighth Conference", Event.randomDate(),
                Event.randomDate(), "Seattle, WA, USA", "Random Street",
                "Eximeeting", descriptionText)
            events.add(event)
            event = Event(
                Random.nextInt(), "Ninth Conference", Event.randomDate(),
                Event.randomDate(), "New York, NY, USA", "Random Square",
                "Eximeeting", descriptionText)
            events.add(event)
            event = Event(
                Random.nextInt(), "Tenth Conference", Event.randomDate(),
                Event.randomDate(), "Albany, NY, USA", "Random Road",
                "Eximeeting", descriptionText)
            events.add(event)

            val databaseReference = FirebaseDatabase
                .getInstance().getReference("Events")
            for (element in events) {
                databaseReference.child(element.id.toString()).setValue(Gson().toJson(element))
            }
        }

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

            return events
        }
    }
}