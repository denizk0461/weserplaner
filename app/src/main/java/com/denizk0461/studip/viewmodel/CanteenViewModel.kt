package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.data.StwParser
import com.denizk0461.studip.model.*

/**
 * View model for [com.denizk0461.studip.fragment.CanteenFragment]
 *
 * @param app   reference to the app
 */
class CanteenViewModel(app: Application) : AppViewModel(app) {

    // Instantiate a parser, should the user want to refresh the canteen offers
    private val parser = StwParser(app)

    /**
     * Retrieves all Stud.IP events.
     *
     * @return all Stud.IP events exposed through a LiveData object
     */
    val allOffers: LiveData<List<CanteenOffer>> = repo.allOffers

    /**
     * Retrieve the user's dietary preferences.
     *
     * @return dietary preferences
     */
    fun getDietaryPrefs(): DietaryPreferences.Object = repo.getDietaryPrefsAsObject()

    /**
     * Retrieves all canteen offer date objects.
     *
     * @return a list of instances of canteen offer dates
     */
    fun getDates(): List<OfferDate> = returnBlocking { repo.getDates() }

    /**
     * Retrieves the opening hours of the canteen as a string.
     *
     * @return  opening hours
     */
    fun getCanteenOpeningHours(): String = returnBlocking { repo.getCanteenOpeningHours() }

    /**
     * Fetch the canteen offers asynchronously. Updates will be provided through a LiveData object.
     *
     * @param canteen           canteen from which to fetch offers from
     * @param onRefreshUpdate   action to execute when a status update is available
     * @param onFinish          action to execute when the operation has finished
     */
    fun fetchOffers(canteen: Int, onRefreshUpdate: (status: Int) -> Unit, onFinish: () -> Unit) {
        doAsync { parser.parse(canteen, onRefreshUpdate, onFinish) }
    }

    /**
     * Updates a given dietary preference to a new value.
     *
     * @param pref      preference to be updated
     * @param newValue  value to set the preference to
     */
    fun setPreference(pref: DietaryPreferences, newValue: Boolean) {
        repo.setPreference(pref, newValue)
    }

    /**
     * Retrieves a single user-specified dietary preference.
     *
     * @param pref  the dietary preference that should be retrieved
     * @return      whether the preference needs to be met
     */
    fun getPreference(pref: DietaryPreferences): Boolean = repo.getBooleanPreference(pref)

    /**
     * This value determines whether the user wants to have allergens marked.
     */
    val preferenceAllergen: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.ALLERGEN, defaultValue = true)

    /**
     * This value determines which canteen the user has selected.
     */
    var preferenceCanteen: Int
        get() = repo.getIntPreference(SettingsPreferences.CANTEEN)
        set(newValue) { repo.setPreference(SettingsPreferences.CANTEEN, newValue) }
}