package com.denizk0461.studip.viewmodel

import android.app.Application

/**
 * View model for [com.denizk0461.studip.fragment.SettingsFragment]
 *
 * @param app   reference to the app
 */
class SettingsViewModel(app: Application) : AppViewModel(app) {

    /**
     * This value determines whether the user wants to have allergens marked.
     */
    var preferenceAllergen: Boolean
        get() = repo.getPreferenceAllergen()
        set(value) { repo.setPreferenceAllergen(value) }
}