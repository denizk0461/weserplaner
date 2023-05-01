package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.model.CanteenOffer
import com.denizk0461.studip.model.DietaryPreferences
import com.denizk0461.studip.model.SettingsPreferences

class CanteenPageViewModel(app: Application) : AppViewModel(app) {

    /**
     * Retrieves all canteen offers that match given day.
     *
     * @return all canteen offers matching the given day exposed through a LiveData object
     */
    fun getOffersByDay(day: Int): LiveData<List<CanteenOffer>> =
        repo.getOffersByDay(day)

    val dietaryPreferencesUpdate: LiveData<Int> = repo.dietaryPreferencesUpdate

    /**
     * Retrieve the user's dietary preferences.
     *
     * @return dietary preferences
     */
    fun getDietaryPrefs(): DietaryPreferences.Object = repo.getDietaryPrefsAsObject()

    /**
     * This value determines whether the user wants to have allergens marked.
     */
    val preferenceAllergen: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.ALLERGEN, defaultValue = true)

    /**
     * This value determines which canteen the user has selected.
     */
    var preferenceColour: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.COLOUR_PREFS)
        set(newValue) { repo.setPreference(SettingsPreferences.COLOUR_PREFS, newValue) }
}