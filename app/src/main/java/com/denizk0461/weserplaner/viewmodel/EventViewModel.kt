package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData

/**
 * View model for [com.denizk0461.weserplaner.fragment.EventFragment]
 *
 * @param app   reference to the app
 */
class EventViewModel(app: Application) : AppViewModel(app) {

    /**
     * Retrieves the number of Stud.IP events available in the database.
     *
     * @return number of Stud.IP events
     */
    fun getEventCount(): LiveData<Int> = repo.getEventCount()

    /**
     * This value determines whether the user wants their timetable to launch with the current day.
     */
    val preferenceCurrentDay: Boolean
        get() = repo.getPreferenceCurrentDay()
}