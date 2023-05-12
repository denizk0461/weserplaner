package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.weserplaner.model.AllergenPreferences
import com.denizk0461.weserplaner.model.CanteenOffer
import com.denizk0461.weserplaner.model.DietaryPreferences
import com.denizk0461.weserplaner.model.SettingsPreferences

class CanteenPageViewModel(app: Application) : AppViewModel(app) {

    /**
     * Retrieves all canteen offers that match given day.
     *
     * @return all canteen offers matching the given day exposed through a LiveData object
     */
    fun getOffersByDay(day: Int): LiveData<List<CanteenOffer>> =
        repo.getOffersByDay(day)

    /**
     * Used to observe whenever updates are made to the dietary preferences.
     */
    val dietaryPreferencesUpdate: LiveData<Int> = repo.dietaryPreferencesUpdate

    /**
     * Retrieve the user's dietary preferences.
     *
     * @return dietary preferences
     */
    fun getDietaryPrefs(): DietaryPreferences.Object = repo.getDietaryPrefsAsObject()

    /**
     * This value determines which allergens the user wants to have displayed or hidden.
     */
    val preferenceAllergenConfig: String
        get() = repo.getStringPreference(
            SettingsPreferences.ALLERGEN_CONFIG, defaultValue = AllergenPreferences.TEMPLATE
        )

    /**
     * This value determines whether the user wants to have allergens marked.
     */
    val preferenceAllergen: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.ALLERGEN, defaultValue = true)

    /**
     * This value determines which canteen the user has selected.
     */
    val preferenceColour: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.COLOUR_PREFS, defaultValue = true)
}