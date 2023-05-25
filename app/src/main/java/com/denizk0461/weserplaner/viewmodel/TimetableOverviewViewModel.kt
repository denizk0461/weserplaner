package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.weserplaner.model.StudIPEvent

class TimetableOverviewViewModel(application: Application) : AppViewModel(application) {

    /**
     * Retrieve all Stud.IP events from the database.
     *
     * @return  all events
     */
    fun getAllEvents(): LiveData<List<StudIPEvent>> = repo.getAllEvents()
}