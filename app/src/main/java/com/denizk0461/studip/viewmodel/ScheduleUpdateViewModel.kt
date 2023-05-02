package com.denizk0461.studip.viewmodel

import android.app.Application
import com.denizk0461.studip.model.StudIPEvent

class ScheduleUpdateViewModel(application: Application) : AppViewModel(application) {

    /**
     * Updates a schedule element.
     *
     * @param event the event to update
     */
    fun update(event: StudIPEvent) { doAsync { repo.update(event) } }

    /**
     * Deletes a schedule element.
     *
     * @param event the event to delete
     */
    fun delete(event: StudIPEvent) { doAsync { repo.delete(event) } }
}