package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import com.denizk0461.weserplaner.model.AllergenPreferences
import com.denizk0461.weserplaner.model.SettingsPreferences

class AllergenConfigViewModel(application: Application) : AppViewModel(application) {

    /**
     * This value determines which allergens the user wants to have displayed or hidden.
     */
    var preferenceAllergenConfig: String
        get() = repo.getStringPreference(
            SettingsPreferences.ALLERGEN_CONFIG, defaultValue = AllergenPreferences.TEMPLATE
        )
        set(newValue) { repo.setPreference(SettingsPreferences.ALLERGEN_CONFIG, newValue) }
}