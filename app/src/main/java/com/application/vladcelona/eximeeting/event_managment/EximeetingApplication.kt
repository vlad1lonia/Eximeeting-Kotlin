package com.application.vladcelona.eximeeting.event_managment

import android.app.Application
import com.application.vladcelona.eximeeting.database.EventRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class EximeetingApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { EventRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { EventRepository(database.eventDao()) }
}