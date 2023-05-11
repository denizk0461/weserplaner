package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import com.denizk0461.weserplaner.model.SettingsPreferences

/**
 * View model for [com.denizk0461.weserplaner.fragment.EventFragment]
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

    /**
     * Determines whether the user has done something to modify their schedule.
     */
    val preferenceHasModifiedSchedule: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.HAS_MODIFIED_SCHEDULE)
}