package com.application.vladcelona.eximeeting.data_classes.event

import com.application.vladcelona.eximeeting.data_classes.user.FirebaseUser
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.jsonToArrayList
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.stringToDate
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.jsonToMap
import java.util.ArrayList
import java.util.LinkedHashMap


data class FirebaseEvent(
    // Event ID
    val id: String = "",

    // Data shown in Event RecyclerView
    val language: String = "",
    val name: String = "",
    val fromDate: String = "",
    val toDate: String = "",
    val location: String = "",
    val address: String = "",
    val organizer: String = "",

    // Additional data shown in the [EventFragment.kt]
    val description: String = "",
    val speakers: String = "",
    val moderators: String = "",
    val businessProgramme: String = "",
    val maps: String = ""
) {

    /**
     * Method for converting [FirebaseEvent] class object from Firestore to [Event] type class
     * @return A new instance of [Event]
     * @author Balandin (Vladcelona) Vladislav
     */
    fun convert(): Event {

        return Event(
            id.toInt(), language, name,
            stringToDate(fromDate), stringToDate(toDate),
            location, address, organizer, description,
            jsonToArrayList(speakers), jsonToArrayList(moderators),
            jsonToMap(businessProgramme) as LinkedHashMap<String, ArrayList<String?>?>?,
            jsonToArrayList(maps)
        )
    }

    /**
     * Method for converting [FirebaseEvent] class object to [HashMap] object for
     * pushing data to Cloud Firebase
     * @return A new instance of [HashMap]
     * @author Balandin (Vladcelona) Vladislav
     */
    fun toMap(): HashMap<String, String> {

        val converted: HashMap<String, String> = HashMap()

        converted["id"] = id
        converted["language"] = language
        converted["name"] = name
        converted["fromDate"] = fromDate
        converted["toDate"] = toDate
        converted["location"] = location
        converted["address"] = address
        converted["organizer"] = organizer
        converted["description"] = description
        converted["speakers"] = speakers
        converted["moderators"] = moderators
        converted["businessProgramme"] = businessProgramme
        converted["maps"] = maps

        return converted
    }
}