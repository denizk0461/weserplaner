package com.denizk0461.studip.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.denizk0461.studip.model.StudIPEvent

@Dao
interface EventDAO {

    @get:Query("SELECT * FROM events ORDER BY id")
    val allEvents: LiveData<List<StudIPEvent>>

    @Insert
    fun insertEvent(event: StudIPEvent)

    @Insert
    fun insertEvents(event: List<StudIPEvent>)

    @Query("DELETE FROM events")
    fun nukeEvents()
}