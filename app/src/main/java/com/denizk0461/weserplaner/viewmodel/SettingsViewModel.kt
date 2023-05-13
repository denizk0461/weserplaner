package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import com.denizk0461.weserplaner.model.AllergenPreferences
import com.denizk0461.weserplaner.model.SettingsPreferences

/**
 * View model for [com.denizk0461.weserplaner.fragment.SettingsFragment]
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
            defaultValue = true,
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
     * This value determines how many allergens the user wants to have displayed or hidden.
     */
    val preferenceAllergenConfigCount: Int
        get() = repo.getStringPreference(
            SettingsPreferences.ALLERGEN_CONFIG, defaultValue = AllergenPreferences.TEMPLATE
        ).run {
            if (isBlank()) 0 else split(",").count()
        }

    /**
     * This value determines which fragment the user wants the app to start with. Order is equal to
     * the order that the items are arranged in in the bottom nav bar.
     */
    var preferencePricing: Int
        get() = repo.getIntPreference(SettingsPreferences.PRICING)
        set(newValue) { repo.setPreference(SettingsPreferences.PRICING, newValue) }

    /**
     * This value determines which fragment the user wants the app to start with. Order is equal to
     * the order that the items are arranged in in the bottom nav bar.
     */
    var preferenceLaunchFragment: Int
        get() = repo.getIntPreference(SettingsPreferences.LAUNCH_FRAGMENT_ON_START)
        set(newValue) { repo.setPreference(SettingsPreferences.LAUNCH_FRAGMENT_ON_START, newValue) }

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

    /**
     * This value determines whether the user has enabled experimental settings.
     */
    var preferenceExperimentalSettingsEnabled: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.EXPERIMENTAL_ENABLED)
        set(newValue) { repo.setPreference(SettingsPreferences.EXPERIMENTAL_ENABLED, newValue) }

    // --- functions for dev codes --- //

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
}