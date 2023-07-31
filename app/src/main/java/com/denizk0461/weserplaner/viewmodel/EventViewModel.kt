package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.weserplaner.values.AppLayout

/**
 * View model for [com.denizk0461.weserplaner.fragment.EventFragment]
 *
 * @param app   reference to the app
 */
class EventViewModel(private val app: Application) : AppViewModel(app) {

    /**
     * Retrieves the number of Stud.IP events available in the database.
     *
     * @return number of Stud.IP events
     */
    fun getEventCount(): LiveData<Int> = repo.getEventCount()

    fun getSelectedTimetableName(): String = returnBlocking {
        try {
            repo.getTimetableForId(repo.getPreferenceSelectedTimetable()).name
        } catch (e: NullPointerException) {
            ""
        }
    }

    /**
     * This value determines whether the user wants their timetable to launch with the current day.
     */
    val preferenceCurrentDay: Boolean
        get() = repo.getPreferenceCurrentDay()

    /**
     * Determines which layout the user wants the app to use.
     */
    val preferenceAppLayout: AppLayout
        get() = repo.getAppLayout()
}