package com.denizk0461.weserplaner.viewmodel

import android.app.Application

class AllergenConfigViewModel(application: Application) : AppViewModel(application) {

    /**
     * This value determines which allergens the user wants to have displayed or hidden.
     */
    var preferenceAllergenConfig: String
        get() = repo.getPreferenceAllergenConfig()
        set(value) { repo.setPreferenceAllergenConfig(value) }
}