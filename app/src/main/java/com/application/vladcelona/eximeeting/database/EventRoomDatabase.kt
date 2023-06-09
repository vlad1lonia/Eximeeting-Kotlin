package com.application.vladcelona.eximeeting.database

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.application.vladcelona.eximeeting.BuildConfig
import com.application.vladcelona.eximeeting.data_classes.event.Event
import com.application.vladcelona.eximeeting.firebase.EximeetingFirebase
import com.application.vladcelona.eximeeting.firebase.EximeetingFirebase.Companion.getCollection
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "EventRoomDatabase"
private const val DATABASE_NAME = "event_database"

private const val appVersion = BuildConfig.VERSION_CODE

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(EventConverters::class)
abstract class EventRoomDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    private class EventDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        private fun deleteDatabase(context: Context, databaseName: String) {
            val databases = File(context.applicationInfo.dataDir + "/databases")
            val db = File(databases, databaseName)
            if (db.delete()) {
                Log.i(TAG, "Database $databaseName deleted")
            } else {
                Log.i(TAG , "Failed to delete database $databaseName")
            }
        }

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.eventDao())
                }
            }
        }

        suspend fun populateDatabase(eventDao: EventDao) {

            val events: ArrayList<Event> = getCollection("events") as ArrayList<Event>
            withContext(Dispatchers.IO) { Thread.sleep(1500) }
            for (event in events) {
                eventDao.insert(event)
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: EventRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): EventRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    EventRoomDatabase::class.java, DATABASE_NAME)
                    .addCallback(EventDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}