package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.weserplaner.model.SettingsPreferences

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
        get() = repo.getBooleanPreference(
            SettingsPreferences.CURRENT_DAY,
            defaultValue = true,
        )

    /**
     * Determines whether the user is launching the app for the first time.
     */
    var preferenceFirstLaunch: Boolean
        get() = repo.getBooleanPreference(
            SettingsPreferences.FIRST_LAUNCH,
            defaultValue = true,
        )
        set(value) { repo.setPreference(SettingsPreferences.FIRST_LAUNCH, value) }
}