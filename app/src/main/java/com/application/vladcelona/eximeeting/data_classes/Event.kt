package com.application.vladcelona.eximeeting.data_classes

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.random.Random

private const val TAG = "Event"

@Entity(tableName = "event_table")
data class Event(
    // Event ID
    @PrimaryKey val id: Int = Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE),

    // Data shown in Event RecyclerView
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "fromDate") val fromDate: Date = Date(),
    @ColumnInfo(name = "toDate") val toDate: Date = Date(),
    @ColumnInfo(name = "location") val location: String = "",
    @ColumnInfo(name = "address") val address: String = "",
    @ColumnInfo(name = "organizer") val organizer: String = "",

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
        return when (getStatusCode()) {
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
     * Method for getting the color for a certain statusCode value
     * @return A new instance of Int (Code of Color)
     */
    fun getStatusCodeColor(): Int {
        return when (getStatusCode()) {
            0 -> Color.YELLOW
            1 -> Color.GREEN
            2 -> Color.GREEN
            3 -> Color.rgb(255,140,0)
            4 -> Color.RED
            else -> {
                Log.i(TAG, "Error while getting statusCode")
                Color.BLACK
            }
        }
    }

    /**
     * Method for getting a statusCode using Date objects
     * @return A new instance of Int
     */
    @SuppressLint("SimpleDateFormat")
    fun getStatusCode(): Int {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val formattedDate: Date = dateFormatter.parse(dateFormatter.format(Date())) as Date

        // The event has ended, so statusCode equals 4
        if (formattedDate.after(toDate)) { return 4 }
        // The event has not started yet, so statusCode equals 0
        if (formattedDate.before(fromDate)) { return 0 }
        // The event has started one day ago, so statusCode equals 1
        return if (abs(TimeUnit.DAYS.convert(fromDate.time - formattedDate.time,
                TimeUnit.MILLISECONDS)) < 1) {
            1
        } else if (abs(TimeUnit.DAYS.convert(toDate.time - formattedDate.time,
                TimeUnit.MILLISECONDS)) < 1) {
            // The event ends in 1 day, so statusCode equals 3
            3
        } else {
            // In other situations, statusCode equals 2
            2
        }
    }

    /**
     * Method for converting the data class into teh Bundle for displaying
     * @return A new instance of Bundle
     */
    fun toBundle(): Bundle {
        return bundleOf(
            "name" to name, "date" to "${dateToString(fromDate)} - \n${dateToString(toDate)}",
            "location" to location, "address" to address, "organizer" to organizer,
            "status" to getStatusCode(), "description" to description, "speakers" to speakers,
            "moderators" to moderators, "businessProgramme" to businessProgramme, "maps" to maps
        )
    }

    /**
     * Method for converting Event class object to Map object with key of String
     * @return A new instance of Map
     */

    fun toMap(): Map<String, Any> {
        val convertedEvent: HashMap<String, Any> = HashMap()

        convertedEvent["name"] = name
        convertedEvent["fromDate"] = fromDate
        convertedEvent["toDate"] = toDate
        convertedEvent["location"] = location
        convertedEvent["address"] = address
        convertedEvent["organizer"] = organizer
        convertedEvent["description"] = description
        convertedEvent["speakers"] = speakers.toString()
        convertedEvent["moderators"] = moderators.toString()
        convertedEvent["businessProgramme"] = businessProgramme.toString()
        convertedEvent["maps"] = maps.toString()

        return convertedEvent
    }

    companion object {
        fun randomDate(): Date {
            return Date(Random.nextLong(1672520400000L, 1685566800000L))
        }

        @SuppressLint("SimpleDateFormat")
        fun randomDateToString(randomDate: Date): String {
            return SimpleDateFormat("dd-MM-yyyy HH:mm").format(randomDate)
        }

        /**
         * Method for converting String object with pattern "dd-MM-yyyy HH:mm" to Date object
         * @return A new instance of Date
         */
        @SuppressLint("SimpleDateFormat")
        fun stringToDate(dateString: String?): Date? {
            return dateString?.let { SimpleDateFormat("dd-MM-yyyy HH:mm").parse(it) }
        }

        /**
         * Method for converting Date object with pattern "dd-MM-yyyy HH:mm" to String object
         * @return A new instance of String
         */
        @SuppressLint("SimpleDateFormat")
        fun dateToString(date: Date?): String? {
            return date?.let { SimpleDateFormat("dd-MM-yyyy HH:mm").format(it) }
        }

        /**
         * Method for converting statusCode field into the String
         * @return A new instance of String
         */
        fun convertStatusCode(statusCode: Int?): String {
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
         * Method for getting the color for a certain statusCode value
         * @return A new instance of Int (Code of Color)
         */
        fun getStatusCodeColor(statusCode: Int?): Int {
            return when (statusCode) {
                0 -> Color.YELLOW
                1 -> Color.GREEN
                2 -> Color.GREEN
                3 -> Color.rgb(255,140,0)
                4 -> Color.RED
                else -> {
                    Log.i(TAG, "Error while getting statusCode")
                    Color.BLACK
                }
            }
        }
    }
}