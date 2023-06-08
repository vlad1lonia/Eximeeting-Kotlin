package com.application.vladcelona.eximeeting.firebase

import android.annotation.SuppressLint
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date

class FirebaseConverters {

    companion object {

        /**
         * Method for converting [String] object with pattern "dd-MM-yyyy HH:mm" to [Date] object
         * @return A new instance of [Date]
         * @author Balandin (Vladcelona) Vladislav
         */
        @SuppressLint("SimpleDateFormat")
        fun stringToDate(dateString: String): Date {
            return SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString)!!
        }

        /**
         * Method for converting [Date] object with pattern "dd-MM-yyyy HH:mm" to [String] object
         * @return A new instance of [String]
         * @author Balandin (Vladcelona) Vladislav
         */
        @SuppressLint("SimpleDateFormat")
        fun dateToString(date: Date): String {
            return SimpleDateFormat("dd-MM-yyyy HH:mm").format(date)
        }

        /**
         * Method for converting Birth date [String] with pattern "dd/MM/yyyy" to [Date] object
         * @return A new instance of [Date]
         * @author Balandin (Vladcelona) Vladislav
         */
        @SuppressLint("SimpleDateFormat")
        fun stringToBirthDate(dateString: String): Date {
            return SimpleDateFormat("dd/MM/yyyy").parse(dateString)!!
        }

        /**
         * Method for converting Birth date [Date] with pattern "dd/MM/yyyy" to [String] object
         * @return A new instance of [Date]
         * @author Balandin (Vladcelona) Vladislav
         */
        @SuppressLint("SimpleDateFormat")
        fun birthDateToString(date: Date): String {
            return SimpleDateFormat("dd/MM/yyyy").format(date)
        }

        /**
         * Method for converting [Any] type object to Json string using [Gson]
         * @return A new instance of [String]
         * @author Balandin (Vladcelona) Vladislav
         */
        fun objectToJson(kotlinObject: Any): String {
            return Gson().toJson(kotlinObject)
        }

        /**
         * Method for converting Json string into [HashMap] object
         * @return A new instance of [HashMap]
         * @author Balandin (Vladcelona) Vladislav
         */
        fun jsonToMap(jsonString: String): Map<String, Any> {
            return Gson().fromJson(jsonString,
                HashMap::class.java) as Map<String, Any>
        }

        /**
         * Method for converting Json string object into an [ArrayList] object
         * @return A new instance of [ArrayList]
         * @author Balandin (Vladcelona) Vladislav
         */
        fun jsonToArrayList(inputString: String): ArrayList<String?> {
            val arrayList: ArrayList<String?> = ArrayList()
            for (element in inputString.substring(1, inputString.length - 1).split(",")) {
                arrayList.add(element.trim())
            }

            return arrayList
        }
    }
}