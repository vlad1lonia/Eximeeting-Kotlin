package com.application.vladcelona.eximeeting.data_classes

import android.graphics.Bitmap
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "Event"
@Entity
data class Event(
            // Event id
            @PrimaryKey val id: UUID = UUID.randomUUID(),

            // Data shown in Event RecyclerView
            val eventName: String = "",
            val fromDate: Date = Date(), val toDate: Date = Date(),
            val location: String = "", val address: String = "",
            val organizer: String = "", val eventStatusCode: Int = 0,

            // Data shown when user presses on a certain event
//            val title: String = "", val description: String = "",
//            val speakers: ArrayList<String> = ArrayList(),
//            val moderators: ArrayList<String> = ArrayList(),

            // TODO: Add business programme and map (also add for pavilions if possible)
//            val businessProgramme: String = "",
//            val eventMaps: ArrayList<String> = ArrayList()
) {

    // Function for converting eventStatusCode to the String (Text)
    fun convertEventStatus(): String {
        return when (eventStatusCode) {
            0 -> "Starts soon" // If the event starts within a week
            1 -> "First day"
            2 -> "Ongoing" // If it is not the first or last day of the event
            3 -> "Last day"
            4 -> "Ended"
            else -> {
                Log.i(TAG, "Error while getting eventStatusCode")
                ""
            }
        }
    }

    val photoFileName: String = "IMG$id.jpg"
}
