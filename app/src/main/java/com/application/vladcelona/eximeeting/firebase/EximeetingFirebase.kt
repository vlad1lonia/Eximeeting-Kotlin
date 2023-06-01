package com.application.vladcelona.eximeeting.firebase

import android.util.Log
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlin.random.Random

private const val TAG = "EximeetingFirebase"

class EximeetingFirebase {

    companion object {

        fun getEventData(): List<Event> {

            return emptyList()
        }
    }
}