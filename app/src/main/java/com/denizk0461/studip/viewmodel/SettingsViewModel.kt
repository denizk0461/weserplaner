package com.denizk0461.studip.viewmodel

import android.app.Application
import com.denizk0461.studip.model.SettingsPreferences

/**
 * View model for [com.denizk0461.studip.fragment.SettingsFragment]
 *
 * @param app   reference to the app
 */
class SettingsViewModel(app: Application) : AppViewModel(app) {

    /**
     * This value determines whether the user wants their timetable to launch with the current day.
     */
    var preferenceCurrentDay: Boolean
        get() = repo.getBooleanPreference(
            SettingsPreferences.CURRENT_DAY,
        )
        set(newValue) { repo.setPreference(SettingsPreferences.CURRENT_DAY, newValue) }

    /**
     * This value determines whether the user wants to have the next course in their schedule
     * highlighted.
     */
    var preferenceCourseHighlighting: Boolean
        get() = repo.getBooleanPreference(
            SettingsPreferences.COURSE_HIGHLIGHTING,
            defaultValue = true
        )
        set(newValue) { repo.setPreference(SettingsPreferences.COURSE_HIGHLIGHTING, newValue) }

    /**
     * This value determines whether the user wants to have allergens marked.
     */
    var preferenceAllergen: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.ALLERGEN, defaultValue = true)
        set(newValue) { repo.setPreference(SettingsPreferences.ALLERGEN, newValue) }

    /**
     * This value determines whether the user wants the app to launch with the canteen view.
     */
    var preferenceLaunchCanteen: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.LAUNCH_CANTEEN_ON_START)
        set(newValue) { repo.setPreference(SettingsPreferences.LAUNCH_CANTEEN_ON_START, newValue) }

    /**
     * This value determines which canteen the user has selected.
     */
    var preferenceColour: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.COLOUR_PREFS, defaultValue = true)
        set(newValue) { repo.setPreference(SettingsPreferences.COLOUR_PREFS, newValue) }

    /**
     * This value determines whether the user opts into submitting crash reports.
     */
    var preferenceDataHandling: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.DATA_HANDLING)
        set(newValue) { repo.setPreference(SettingsPreferences.DATA_HANDLING, newValue) }
}