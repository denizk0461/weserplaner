package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import com.denizk0461.weserplaner.model.StudIPEvent

class ScheduleUpdateViewModel(application: Application) : AppViewModel(application) {

    /**
     * Inserts a new schedule element.
     *
     * @param event the event to save
     */
    fun insert(event: StudIPEvent) { doAsync { repo.insert(event) } }

    /**
     * Updates a schedule element.
     *
     * @param event the event to update
     */
    fun update(event: StudIPEvent) { doAsync { repo.update(event) } }

    fun getCurrentTimetableId(): Int = repo.getPreferenceSelectedTimetable()
}