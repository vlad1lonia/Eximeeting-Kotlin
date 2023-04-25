package com.application.vladcelona.eximeeting.event_managment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.database.EventDatabase
import com.application.vladcelona.eximeeting.database.migration_1_2
import java.io.File
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "event-database"

class EventRepository private constructor(context: Context) {

    private val database: EventDatabase = Room.databaseBuilder(
        context.applicationContext, EventDatabase::class.java, DATABASE_NAME)
        .addMigrations(migration_1_2).build()

    private val eventDao = database.eventDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getEvents(): LiveData<List<Event>> = eventDao.getEvents()

    fun getEvent(id: UUID): LiveData<Event?> = eventDao.getEvent(id)

    fun updateEvent(event: Event) {
        executor.execute { eventDao.updateEvent(event) }
    }

    fun addEvent(event: Event) {
        executor.execute { eventDao.addEvent(event) }
    }

    fun getPhotoFile(event: Event): File = File(filesDir, event.photoFileName)

    companion object {
        private var instance: EventRepository? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = EventRepository(context)
            }
        }

        fun get(): EventRepository {
            return instance ?: throw IllegalStateException("Event repository must be initialized")
        }
    }
}