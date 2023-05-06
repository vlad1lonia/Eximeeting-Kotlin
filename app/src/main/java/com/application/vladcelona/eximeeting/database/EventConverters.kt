package com.application.vladcelona.eximeeting.database

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


class EventConverters {

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let { SimpleDateFormat("dd-MM-yyyy HH:mm").format(it) }
    }

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun toDate(dateString: String?): Date? {
        return dateString?.let { SimpleDateFormat("dd-MM-yyyy HH:mm").parse(it) }
    }

    @TypeConverter
    fun fromStringToArrayList(value: String?): ArrayList<String?>? {
        val type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromArrayList(value: ArrayList<String?>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromStringToMap(value: String?): Map<String, ArrayList<String?>?> {
        val type = object : TypeToken<Map<String, ArrayList<String?>?>?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromMap(value: Map<String, ArrayList<String?>?>?): String? {
        return Gson().toJson(value)
    }

}