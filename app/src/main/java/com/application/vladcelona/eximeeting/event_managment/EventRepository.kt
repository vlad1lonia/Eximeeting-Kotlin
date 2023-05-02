package com.application.vladcelona.eximeeting.event_managment

import androidx.annotation.WorkerThread
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.database.EventDao
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {

    val events: Flow<List<Event>> = eventDao.getEvents()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(event: Event) {
        eventDao.insert(event)
    }
}