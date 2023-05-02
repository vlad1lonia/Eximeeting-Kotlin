package com.application.vladcelona.eximeeting.data_classes

import android.graphics.Bitmap
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import javax.annotation.Nonnull
import kotlin.collections.ArrayList

private const val TAG = "Event"

@Entity(tableName = "event_table")
data class Event(
            // Event ID
            @PrimaryKey(autoGenerate = true) val id: Int,

            // Data shown in Event RecyclerView
            @ColumnInfo(name = "name") var name: String = "",
            @ColumnInfo(name = "fromDate") var fromDate: String = "",
            @ColumnInfo(name = "toDate") var toDate: String = "",
            @ColumnInfo(name = "location") var location: String = "",
            @ColumnInfo(name = "address") var address: String = "",
            @ColumnInfo(name = "organizer") var organizer: String = "",
            @ColumnInfo(name = "statusCode") var statusCode: Int = 0

            // Data shown when user presses on a certain event
//            val title: String = "", val description: String = "",
//            val speakers: ArrayList<String> = ArrayList(),
//            val moderators: ArrayList<String> = ArrayList(),
            // TODO: Add business programme and map (also add for pavilions if possible)
//            val businessProgramme: String = "",
//            val eventMaps: ArrayList<String> = ArrayList()
) {

    // Function for converting statusCode to the String (Text)
    fun convertStatusCode(): String {
        return when (statusCode) {
            0 -> "Starts soon" // If the event starts within a week
            1 -> "First day"
            2 -> "Ongoing" // If it is not the first or last day of the event
            3 -> "Last day"
            4 -> "Ended"
            else -> {
                Log.i(TAG, "Error while getting statusCode")
                ""
            }
        }
    }

//    val photoFileName: String = "IMG$id.jpg"
}
