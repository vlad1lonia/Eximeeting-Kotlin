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

            val businessProgramme: LinkedHashMap<String, ArrayList<String?>?>? = LinkedHashMap()
            businessProgramme?.set("Monday", """9:00 AM - 10:30 AM: Financial Analysis Meeting
10:30 AM - 11:30 AM: Sales Training Session
11:30 AM - 12:30 PM: Brainstorming Session
12:30 PM - 1:30 PM: Lunch Break
1:30 PM - 3:00 PM: Team Building Activity
3:00 PM - 4:30 PM: Client Presentation Preparation
4:30 PM - 5:30 PM: Project Evaluation Meeting""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgramme?.set("Tuesday", """9:00 AM - 10:00 AM: Team Meeting
10:00 AM - 11:00 AM: Project Planning Session
11:00 AM - 12:00 PM: Client Call
12:00 PM - 1:00 PM: Lunch Break
1:00 PM - 3:00 PM: Marketing Strategy Workshop
3:00 PM - 4:00 PM: Product Development Discussion
4:00 PM - 5:00 PM: Networking Event""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgramme?.set("Wednesday", """9:00 AM - 10:00 AM: Departmental Meeting
10:00 AM - 11:00 AM: Market Research Analysis
11:00 AM - 12:00 PM: Product Design Review
12:00 PM - 1:00 PM: Lunch Break
1:00 PM - 2:30 PM: Business Development Discussion
2:30 PM - 4:00 PM: Training Workshop
4:00 PM - 5:00 PM: Team Collaboration Session""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgramme?.set("Thursday", """9:00 AM - 10:30 AM: Sales Pitch Practice
10:30 AM - 11:30 AM: Vendor Meeting
11:30 AM - 12:30 PM: Customer Feedback Analysis
12:30 PM - 1:30 PM: Lunch Break
1:30 PM - 3:00 PM: Project Status Update
3:00 PM - 4:30 PM: Strategic Planning Session
4:30 PM - 5:30 PM: Networking Event""".trim().split("\n").toList() as ArrayList<String?>?
            )
            businessProgramme?.set("Friday", """9:00 AM - 10:00 AM: Team Stand-up Meeting
10:00 AM - 11:00 AM: Marketing Campaign Review
11:00 AM - 12:00 PM: Sales Report Analysis
12:00 PM - 1:00 PM: Lunch Break
1:00 PM - 3:00 PM: Client Meeting
3:00 PM - 4:30 PM: Product Demo
4:30 PM - 5:30 PM: Project Retrospective""".trim().split("\n").toList() as ArrayList<String?>?
            )

            val events: ArrayList<Event> = ArrayList()

            var event = Event(
                Random.nextInt(), "First Conference", Event.randomDate(),
                Event.randomDate(), "Moscow, Russia", "117892, Random Street",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "Second Conference", Event.randomDate(),
                Event.randomDate(), "Washington D.C., USA", "Random Square",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "Third Conference", Event.randomDate(),
                Event.randomDate(), "Madrid, Spain", "Random Square",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "Fourth Conference", Event.randomDate(),
                Event.randomDate(), "Casablanca, Morocco", "Random Square",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "Fifth Conference", Event.randomDate(),
                Event.randomDate(), "Minsk, Belarus", "Random Drive",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "Sixth Conference", Event.randomDate(),
                Event.randomDate(), "Los Angeles, CA, USA", "Random Road",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "Seventh Conference", Event.randomDate(),
                Event.randomDate(), "Washington D.C., USA", "Random Event",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "Eighth Conference", Event.randomDate(),
                Event.randomDate(), "Seattle, WA, USA", "Random Street",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "Ninth Conference", Event.randomDate(),
                Event.randomDate(), "New York, NY, USA", "Random Square",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
            events.add(event)
            event = Event(
                Random.nextInt(), "Tenth Conference", Event.randomDate(),
                Event.randomDate(), "Albany, NY, USA", "Random Road",
                "Eximeeting", descriptionText, Event.randomSpeakers(), Event.randomModerators(), businessProgramme)
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