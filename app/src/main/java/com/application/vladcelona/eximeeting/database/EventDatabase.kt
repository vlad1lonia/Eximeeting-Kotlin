package com.application.vladcelona.eximeeting.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.application.vladcelona.eximeeting.data_classes.Event

@Database(entities = [ Event::class ], version = 2)
@TypeConverters(EventTypeConverters::class)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE Event ADD COLUMN eventName TEXT NOT NULL DEFAULT ''"
        )
    }
}