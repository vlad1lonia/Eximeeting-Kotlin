package com.application.vladcelona.eximeeting.data_classes

import android.util.Log
import java.util.*

private const val TAG = "Event"
data class Event(
            // Event id
            val id: UUID = UUID.randomUUID(),

            // Data shown in Event RecyclerView
            val eventName: String = "",
            val fromDate: Date = Date(), val toDate: Date = Date(),
            val location: String = "", val address: String = "",
            val organizer: String = "", val eventStatusCode: Int = 0,

            // Data shown when user presses on a certain event
            var title: String = "", var description: String = "",
            val speakers: ArrayList<String> = ArrayList(),
            val moderators: ArrayList<String> = ArrayList(),

            // TODO: Add business programme and map (also add for pavilions if possible)
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
}
