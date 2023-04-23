package com.denizk0461.studip.viewmodel

import android.app.Application
import com.denizk0461.studip.model.StudIPEvent

/**
 * View model for [com.denizk0461.studip.activity.FetcherActivity]
 *
 * @param app   reference to the app
 */
class FetcherViewModel(app: Application) : AppViewModel(app) {

    /**
     * Save a list of Stud.IP events to persistent storage asynchronously.
     *
     * @param events    list of events to be saved
     */
    fun insertEvents(events: List<StudIPEvent>) { doAsync { repo.insertEvents(events) } }

    /**
     * Delete all Stud.IP events from the database asynchronously.
     */
    fun nukeEvents() { doAsync { repo.nukeEvents() } }
}