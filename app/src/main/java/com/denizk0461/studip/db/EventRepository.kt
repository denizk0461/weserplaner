package com.denizk0461.studip.db

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.model.StudIPEvent

class EventRepository(app: Application) {

    private val dao: EventDAO = EventDatabase.getInstance(app.applicationContext).dao()

    val allEvents: LiveData<List<StudIPEvent>> = dao.allEvents

    fun nukeEvents() { dao.nukeEvents() }
    fun insertEvent(event: StudIPEvent) { dao.insertEvent(event) }
    fun insertEvents(events: List<StudIPEvent>) { dao.insertEvents(events) }
}