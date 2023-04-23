package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.model.StudIPEvent

/**
 * View model for [com.denizk0461.studip.fragment.EventFragment]
 *
 * @param app   reference to the app
 */
class EventViewModel(app: Application) : TemplateViewModel(app) {

    /**
     * Retrieves all Stud.IP events.
     *
     * @return all Stud.IP events exposed through a LiveData object
     */
    val allEvents: LiveData<List<StudIPEvent>> = repo.allEvents
}