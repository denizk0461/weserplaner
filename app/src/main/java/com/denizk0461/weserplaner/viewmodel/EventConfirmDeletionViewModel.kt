package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import com.denizk0461.weserplaner.model.StudIPEvent

class EventConfirmDeletionViewModel(application: Application) : AppViewModel(application) {

    /**
     * Deletes a schedule element.
     *
     * @param event the event to delete
     */
    fun delete(event: StudIPEvent) { doAsync { repo.delete(event) } }
}