package com.denizk0461.studip.viewmodel

import android.app.Application
import com.denizk0461.studip.model.StudIPEvent

class FetcherViewModel(app: Application) : TemplateViewModel(app) {

    fun insertEvent(event: StudIPEvent) { repo.insertEvent(event) }
    fun insertEvents(events: List<StudIPEvent>) { doAsync { repo.insertEvents(events) } }
    fun nukeEvents() { doAsync { repo.nukeEvents() } }
}