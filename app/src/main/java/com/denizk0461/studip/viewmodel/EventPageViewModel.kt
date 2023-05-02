package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.model.StudIPEvent

/**
 * View model for [com.denizk0461.studip.fragment.EventPageFragment]
 *
 * @param app   reference to the app
 */
class EventPageViewModel(app: Application) : AppViewModel(app) {

    /**
     * Retrieves Stud.IP events for a specific day, ordered by their timeslots, then their IDs.
     *
     * @return  Stud.IP events for a certain day exposed through a LiveData object
     */
    fun getEventsForDay(day: Int): LiveData<List<StudIPEvent>> = repo.getEventsForDay(day)
}