package com.denizk0461.studip.viewmodel

import android.app.Application
import com.denizk0461.studip.model.SettingsPreferences

/**
 * View model for [com.denizk0461.studip.fragment.EventFragment]
 *
 * @param app   reference to the app
 */
class EventViewModel(app: Application) : AppViewModel(app) {

    /**
     * This value determines whether the user wants their timetable to launch with the current day.
     */
    val preferenceCurrentDay: Boolean
        get() = repo.getBooleanPreference(
            SettingsPreferences.CURRENT_DAY,
            defaultValue = true
        )
}