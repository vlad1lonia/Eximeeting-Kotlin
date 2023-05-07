package com.application.vladcelona.eximeeting.firebase

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.application.vladcelona.eximeeting.data_classes.Event
import com.google.firebase.database.Exclude
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

data class FirebaseEvent(
    // Event ID
    val id: Int = Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE),

    // Data shown in Event RecyclerView
    val name: String = "",
    val fromDate: String = "",
    val toDate: String = "",
    val location: String = "",
    val address: String = "",
    val organizer: String = "",

    // Data shown when user presses on a certain event
    val description: String = "",
    val speakers: String = "",
    val moderators: String = "",
    val businessProgramme: String = "",
    val maps: String = ""
) {

    fun convertToEvent(): Event {

        return Event(
            id = id,
            name = name,
            fromDate = Event.stringToDate(fromDate)!!,
            toDate = Event.stringToDate(toDate)!!,
            location = location,
            address = address,
            organizer = organizer,
            description = description,
            speakers = Event.stringToArrayList(speakers),
            moderators = Event.stringToArrayList(moderators),
            businessProgramme = Event.stringToMap(businessProgramme),
            maps = Event.stringToArrayList(maps)
        )
    }
}
