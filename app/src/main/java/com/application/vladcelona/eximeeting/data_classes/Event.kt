package com.application.vladcelona.eximeeting.data_classes

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import javax.annotation.Nonnull
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val TAG = "Event"

@Entity(tableName = "event_table")
data class Event(
    // Event ID
    @PrimaryKey(autoGenerate = true) val id: Int,

    // Data shown in Event RecyclerView
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "fromDate") val fromDate: String = "",
    @ColumnInfo(name = "toDate") val toDate: String = "",
    @ColumnInfo(name = "location") val location: String = "",
    @ColumnInfo(name = "address") val address: String = "",
    @ColumnInfo(name = "organizer") val organizer: String = "",
    @ColumnInfo(name = "statusCode") val statusCode: Int = 0,

    // Data shown when user presses on a certain event
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "speakers") val speakers: ArrayList<String?>? = ArrayList(),
    @ColumnInfo(name = "moderators") val moderators: ArrayList<String?>? = ArrayList(),
    @ColumnInfo(name = "businessProgramme")
    val businessProgramme: Map<String, ArrayList<String?>?>? = HashMap(),
    @ColumnInfo(name = "maps") val maps: ArrayList<String?>? = ArrayList()
) {

    /**
     * Method for converting statusCode field into the String
     * @return A new instance of String
     */
    fun convertStatusCode(): String {
        return when (statusCode) {
            0 -> "Starts soon" // If the event starts within a week
            1 -> "First day"
            2 -> "Ongoing" // If it is not the first or last day of the event
            3 -> "Last day"
            4 -> "Ended"
            else -> {
                Log.i(TAG, "Error while getting statusCode")
                "Not mentioned"
            }
        }
    }

    /**
     * Method for converting teh data class into teh Bundle for displaying
     * @return A new instance of Bundle
     */
    fun toBundle(): Bundle {
        return bundleOf(
            "name" to name, "date" to "$fromDate - $toDate", "location" to location,
            "address" to address, "organizer" to organizer, "status" to convertStatusCode(),
            "description" to description, "speakers" to speakers, "moderators" to moderators,
            "businessProgramme" to businessProgramme, "maps" to maps
        )
    }

//    val photoFileName: String = "IMG$id.jpg"
}