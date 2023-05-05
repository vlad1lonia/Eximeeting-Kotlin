package com.application.vladcelona.eximeeting.database

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.util.*


class EventConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
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