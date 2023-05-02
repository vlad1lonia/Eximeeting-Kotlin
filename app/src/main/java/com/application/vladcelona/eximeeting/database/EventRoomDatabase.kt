package com.application.vladcelona.eximeeting.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.application.vladcelona.eximeeting.data_classes.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random

private const val TAG = "EventRoomDatabase"
private const val DATABASE_NAME = "event_database"

@Database(entities = [Event::class], version = 1, exportSchema = false)
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
//                    deleteDatabase(Context.)
                }
            }
        }

        suspend fun populateDatabase(eventDao: EventDao) {
            var event = Event(Random.nextInt(), "First Conference", "02/05/2023",
                "10/05/2023", "Moscow, Russia", "117892, Random Street",
                "Eximeeting", 1)
            eventDao.insert(event)
            event =  Event(Random.nextInt(), "Second Conference", "02/01/2023",
                "10/01/2023", "Washington D.C., USA", "Random Square",
                "Eximeeting", 0)
            eventDao.insert(event)
            event =  Event(Random.nextInt(), "Third Conference", "12/12/2021",
                "15/12/2023", "Madrid, Spain", "Random Square",
                "Eximeeting", 4)
            eventDao.insert(event)
            event =  Event(Random.nextInt(), "Fourth Conference", "02/01/2023",
                "10/01/2023", "Casablanca, Morocco", "Random Square",
                "Eximeeting", 0)
            eventDao.insert(event)
            event =  Event(Random.nextInt(), "Fifth Conference", "02/09/2023",
                "10/09/2023", "Minsk, Belarus", "Random Drive",
                "Eximeeting", 3)
            eventDao.insert(event)
            event =  Event(Random.nextInt(), "Sixth Conference", "02/01/2023",
                "10/01/2023", "Los Angeles, CA, USA", "Random Roas",
                "Eximeeting", 1)
            eventDao.insert(event)
            event =  Event(Random.nextInt(), "Seventh Conference", "02/01/2023",
                "10/01/2023", "Washington D.C., USA", "Random Event",
                "Eximeeting", 0)
            eventDao.insert(event)
            event =  Event(Random.nextInt(), "Eighth Conference", "02/11/2023",
                "10/11/2023", "Seattle, WA, USA", "Random Street",
                "Eximeeting", 3)
            eventDao.insert(event)
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