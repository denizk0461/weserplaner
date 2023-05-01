package com.denizk0461.studip.viewmodel

import android.app.Application
import com.denizk0461.studip.model.AllergenPreferences
import com.denizk0461.studip.model.SettingsPreferences

/**
 * View model for [com.denizk0461.studip.fragment.SettingsFragment]
 *
 * @param app   reference to the app
 */
class SettingsViewModel(app: Application) : AppViewModel(app) {



    /**
     * Deletes everything from the database.
     */
    fun nukeEverything() {
        nukeEvents()
        nukeOfferItems()
        nukeOfferCategories()
        nukeOfferCanteens()
        nukeOfferDates()
    }

    /**
     * Deletes all Stud.IP events from the database.
     */
    fun nukeEvents() { doAsync { repo.nukeEvents() } }

    /**
     * Deletes all canteen items from the database.
     */
    fun nukeOfferItems() { doAsync { repo.nukeOfferItems() } }

    /**
     * Deletes all canteen categories from the database.
     */
    fun nukeOfferCategories() { doAsync { repo.nukeOfferCategories() } }

    /**
     * Deletes all canteens from the database.
     */
    fun nukeOfferCanteens() { doAsync { repo.nukeOfferCanteens() } }

    /**
     * Deletes all canteen dates from the database.
     */
    fun nukeOfferDates() { doAsync { repo.nukeOfferDates() } }

    /**
     * This value determines whether the user wants to have the next course in their schedule
     * highlighted.
     */
    var preferenceCourseHighlighting: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.COURSE_HIGHLIGHTING)
        set(newValue) { repo.setPreference(SettingsPreferences.COURSE_HIGHLIGHTING, newValue) }

    /**
     * This value determines which allergens the user wants to have displayed or hidden.
     */
    var preferenceAllergenConfig: String
        get() = repo.getStringPreference(
            SettingsPreferences.ALLERGEN_CONFIG, defaultValue = AllergenPreferences.TEMPLATE
        )
        set(newValue) { repo.setPreference(SettingsPreferences.ALLERGEN_CONFIG, newValue) }

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
        get() = repo.getBooleanPreference(SettingsPreferences.COLOUR_PREFS)
        set(newValue) { repo.setPreference(SettingsPreferences.COLOUR_PREFS, newValue) }

    /**
     * This value determines whether the user opts into submitting crash reports.
     */
    var preferenceDataHandling: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.DATA_HANDLING)
        set(newValue) { repo.setPreference(SettingsPreferences.DATA_HANDLING, newValue) }
}