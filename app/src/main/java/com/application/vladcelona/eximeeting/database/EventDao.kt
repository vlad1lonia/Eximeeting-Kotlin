package com.application.vladcelona.eximeeting.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.application.vladcelona.eximeeting.data_classes.event.Event
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface EventDao {

    @Query("SELECT * FROM event_table")
    fun getEvents(): Flow<List<Event>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Event)

    @Query("DELETE FROM event_table")
    suspend fun deleteTable()
}