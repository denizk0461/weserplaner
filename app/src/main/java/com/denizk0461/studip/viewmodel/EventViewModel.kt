package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.data.StudIPEvent

class EventViewModel(app: Application) : TemplateViewModel(app) {

    val allEvents: LiveData<List<StudIPEvent>> = repo.allEvents

    fun insertEvent(event: StudIPEvent) { repo.insertEvent(event) }
    fun nukeEvents() { repo.nukeEvents() }
}