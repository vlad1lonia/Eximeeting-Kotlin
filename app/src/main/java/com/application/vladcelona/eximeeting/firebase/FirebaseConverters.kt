package com.application.vladcelona.eximeeting.firebase

import android.annotation.SuppressLint
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.LinkedHashMap

class FirebaseConverters {

    companion object {

        /**
         * Method for converting [String] object with pattern "dd-MM-yyyy HH:mm" to [Date] object
         * @return A new instance of [Date]
         */
        @SuppressLint("SimpleDateFormat")
        fun stringToDate(dateString: String): Date {
            return SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString)!!
        }

        /**
         * Method for converting [Date] object with pattern "dd-MM-yyyy HH:mm" to [String] object
         * @return A new instance of [String]
         */
        @SuppressLint("SimpleDateFormat")
        fun dateToString(date: Date): String {
            return SimpleDateFormat("dd-MM-yyyy HH:mm").format(date)
        }

        /**
         * Method for converting [String] object into an [ArrayList] object
         * @return A new instance of [ArrayList]
         */
        fun stringToArrayList(inputString: String): ArrayList<String?> {
            val arrayList: ArrayList<String?> = ArrayList()
            for (element in inputString.substring(1, inputString.length - 1).split(", ")) {
                arrayList.add(element)
            }

            return arrayList
        }

        /**
         * Method for converting [String] object into an [HashMap] object
         * @return A new instance of [HashMap]
         */
        fun stringToMap(inputString: String): LinkedHashMap<String, ArrayList<String?>?> {
            return Gson().fromJson(
                inputString, HashMap::class.java) as LinkedHashMap<String, ArrayList<String?>?>
        }
    }
}