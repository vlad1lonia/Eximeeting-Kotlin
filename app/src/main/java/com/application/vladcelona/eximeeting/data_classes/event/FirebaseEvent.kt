package com.application.vladcelona.eximeeting.data_classes.event

import com.application.vladcelona.eximeeting.data_classes.user.FirebaseUser
import com.application.vladcelona.eximeeting.data_classes.user.User
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.stringToArrayList
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.stringToDate
import com.application.vladcelona.eximeeting.firebase.FirebaseConverters.Companion.stringToMap


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
     * Method for converting [FirebaseEvent] data class from Firestore to [Event] data class
     * @return A new instance of [Event]
     */
    fun convert(): Event {

        return Event(
            id.toInt(), language, name,
            stringToDate(fromDate), stringToDate(toDate),
            location, address, organizer, description,
            stringToArrayList(speakers), stringToArrayList(moderators),
            stringToMap(businessProgramme), stringToArrayList(maps)
        )
    }
}