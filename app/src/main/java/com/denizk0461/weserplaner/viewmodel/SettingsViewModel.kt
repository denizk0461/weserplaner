package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.weserplaner.model.Timetable
import com.denizk0461.weserplaner.values.AppLayout

/**
 * View model for [com.denizk0461.weserplaner.fragment.SettingsFragment]
 *
 * @param app   reference to the app
 */
class SettingsViewModel(app: Application) : AppViewModel(app) {

    fun getTimetables(): LiveData<List<Timetable>> = repo.getTimetables()

    fun getSelectedTimetable(): Int = repo.getPreferenceSelectedTimetable()
    fun setSelectedTimetable(newValue: Int) = repo.setPreferenceSelectedTimetable(newValue)

    /**
     * This value determines whether the user wants their timetable to launch with the current day.
     */
    var preferenceCurrentDay: Boolean
        get() = repo.getPreferenceCurrentDay()
        set(newValue) { repo.setPreferenceCurrentDay(newValue) }

    /**
     * This value determines whether the user wants to have the next course in their schedule
     * highlighted.
     */
    var preferenceCourseHighlighting: Boolean
        get() = repo.getPreferenceCourseHighlighting()
        set(newValue) { repo.setPreferenceCourseHighlighting(newValue) }

    /**
     * This value determines whether the user wants to have allergens marked.
     */
    var preferenceAllergen: Boolean
        get() = repo.getPreferenceAllergen()
        set(newValue) { repo.setPreferenceAllergen(newValue) }

    /**
     * This value determines how many allergens the user wants to have displayed or hidden.
     */
    val preferenceAllergenConfigCount: Int
        get() = repo.getPreferenceAllergenConfigCount()

    /**
     * This value determines which fragment the user wants the app to start with. Order is equal to
     * the order that the items are arranged in in the bottom nav bar.
     */
    var preferencePricing: Int
        get() = repo.getPreferencePricing()
        set(newValue) { repo.setPreferencePricing(newValue) }

    /**
     * This value determines which fragment the user wants the app to start with. Order is equal to
     * the order that the items are arranged in in the bottom nav bar.
     */
    var preferenceLaunchFragment: Int
        get() = repo.getPreferenceLaunchFragment()
        set(newValue) { repo.setPreferenceLaunchFragment(newValue) }

    /**
     * This value determines which canteen the user has selected.
     */
    var preferenceColour: Boolean
        get() = repo.getPreferenceColour()
        set(newValue) { repo.setPreferenceColour(newValue) }

    /**
     * This value determines whether the user opts into submitting crash reports.
     */
    var preferenceDataHandling: Boolean
        get() = repo.getPreferenceDataHandling()
        set(newValue) { repo.setPreferenceDataHandling(newValue) }

    /**
     * This value determines whether the user has enabled experimental settings.
     */
    var preferenceExperimentalSettingsEnabled: Boolean
        get() = repo.getPreferenceExperimentalSettingsEnabled()
        set(newValue) { repo.setPreferenceExperimentalSettingsEnabled(newValue) }

    /**
     * This value determines whether beta screens should be shown.
     */
    var preferenceBetaScreensEnabled: Boolean
        get() = repo.getPreferenceBetaScreensEnabled()
        set(newValue) { repo.setPreferenceBetaScreensEnabled(newValue) }

    /**
     * Determines whether the user is launching the app for the first time.
     */
    var preferenceFirstLaunch: Boolean
        get() = repo.getPreferenceFirstLaunch()
        set(newValue) { repo.setPreferenceFirstLaunch(newValue) }

    /**
     * Determines which layout the user wants the app to use.
     */
    var preferenceAppLayout: AppLayout
        get() = repo.getAppLayout()
        set(newValue) { repo.setAppLayout(newValue) }

    fun setPreferenceFeatureTimetables(newValue: Boolean) { repo.setPreferenceFeatureTimetables(newValue) }

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