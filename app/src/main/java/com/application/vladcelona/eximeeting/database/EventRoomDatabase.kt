package com.application.vladcelona.eximeeting.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.application.vladcelona.eximeeting.BuildConfig
import com.application.vladcelona.eximeeting.data_classes.Event
import com.application.vladcelona.eximeeting.firebase.EximeetingFirebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random

private const val TAG = "EventRoomDatabase"
private const val DATABASE_NAME = "event_database"

@Database(entities = [Event::class], version = BuildConfig.VERSION_CODE, exportSchema = false)
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

            val descriptionText = """Join us for an exciting evening of networking and knowledge sharing at our annual Business Innovation Summit. This event brings together industry leaders, entrepreneurs, and professionals from diverse sectors to explore the latest trends and strategies shaping the business landscape.

Featuring renowned keynote speakers, interactive panel discussions, and breakout sessions, the Business Innovation Summit offers valuable insights into disruptive technologies, market trends, and successful business models. Gain inspiration from thought-provoking presentations and engage in meaningful conversations with like-minded professionals.

Whether you're an aspiring entrepreneur, a seasoned executive, or a business enthusiast, this event provides an excellent platform to expand your network, forge new partnerships, and stay ahead of the curve. Connect with industry experts, discover innovative solutions, and explore opportunities for collaboration in a dynamic and vibrant environment.

Don't miss this opportunity to join the conversation and be part of the business revolution. Reserve your spot at the Business Innovation Summit today and unlock new possibilities for growth, innovation, and success."""

            val events = EximeetingFirebase.getEvents()
            for (event in events) {
                if (event != null) { eventDao.insert(event) }
            }

//            var event = Event(Random.nextInt(), "First Conference", Event.randomDate(),
//                Event.randomDate(), "Moscow, Russia", "117892, Random Street",
//                "Eximeeting", descriptionText)
//            eventDao.insert(event)
//            event =  Event(Random.nextInt(), "Second Conference", Event.randomDate(),
//                Event.randomDate(), "Washington D.C., USA", "Random Square",
//                "Eximeeting", descriptionText)
//            eventDao.insert(event)
//            event =  Event(Random.nextInt(), "Third Conference", Event.randomDate(),
//                Event.randomDate(), "Madrid, Spain", "Random Square",
//                "Eximeeting", descriptionText)
//            eventDao.insert(event)
//            event =  Event(Random.nextInt(), "Fourth Conference", Event.randomDate(),
//                Event.randomDate(), "Casablanca, Morocco", "Random Square",
//                "Eximeeting", descriptionText)
//            eventDao.insert(event)
//            event =  Event(Random.nextInt(), "Fifth Conference", Event.randomDate(),
//                Event.randomDate(), "Minsk, Belarus", "Random Drive",
//                "Eximeeting", descriptionText)
//            eventDao.insert(event)
//            event =  Event(Random.nextInt(), "Sixth Conference", Event.randomDate(),
//                Event.randomDate(), "Los Angeles, CA, USA", "Random Roas",
//                "Eximeeting", descriptionText)
//            eventDao.insert(event)
//            event =  Event(Random.nextInt(), "Seventh Conference", Event.randomDate(),
//                Event.randomDate(), "Washington D.C., USA", "Random Event",
//                "Eximeeting", descriptionText)
//            eventDao.insert(event)
//            event =  Event(Random.nextInt(), "Eighth Conference", Event.randomDate(),
//                Event.randomDate(), "Seattle, WA, USA", "Random Street",
//                "Eximeeting", descriptionText)
//            eventDao.insert(event)
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