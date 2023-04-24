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
     * This value determines whether the user opts into submitting crash reports.
     */
    var preferenceCourseHighlighting: Boolean
        get() = repo.getPreference(SettingsPreferences.DATA_HANDLING)
        set(newValue) { repo.setPreference(SettingsPreferences.DATA_HANDLING, newValue) }

    /**
     * This value determines whether the user wants to have allergens marked.
     */
    var preferenceAllergen: Boolean
        get() = repo.getPreference(SettingsPreferences.ALLERGEN)
        set(newValue) { repo.setPreference(SettingsPreferences.ALLERGEN, newValue) }

    /**
     * This value determines whether the user opts into submitting crash reports.
     */
    var preferenceDataHandling: Boolean
        get() = repo.getPreference(SettingsPreferences.DATA_HANDLING)
        set(newValue) { repo.setPreference(SettingsPreferences.DATA_HANDLING, newValue) }
}