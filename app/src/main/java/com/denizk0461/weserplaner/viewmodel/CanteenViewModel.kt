package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.denizk0461.weserplaner.data.StwParser
import com.denizk0461.weserplaner.values.DietaryPreferences
import com.denizk0461.weserplaner.model.*
import com.denizk0461.weserplaner.values.AppLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * View model for [com.denizk0461.weserplaner.fragment.CanteenFragment]
 *
 * @param app   reference to the app
 */
class CanteenViewModel(app: Application) : AppViewModel(app) {

    // Instantiate a parser, should the user want to refresh the canteen offers
    private val parser = StwParser(app)

    /**
     * Retrieves all canteen offer date objects.
     *
     * @return a list of instances of canteen offer dates
     */
    fun getDates(): LiveData<List<OfferDate>> = repo.getDates()

    /**
     * Fetch the canteen offers asynchronously. Updates will be provided through a LiveData object.
     *
     * @param canteen   canteen from which to fetch offers from
     * @param onFinish  action to execute when the operation has finished
     * @param onError   action to execute when the operation encountered an error
     */
    fun fetchOffers(
        canteen: Int,
        onFinish: () -> Unit,
        onError: () -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            parser.parse(canteen, onFinish, onError)
        }
    }

    /**
     * Retrieves the canteen stored in the database. Should always be 0 or 1.
     *
     * @return canteen
     */
    fun getCanteen(): LiveData<OfferCanteen> = repo.getCanteen()

    /**
     * Register that the user has made an update to their dietary preferences.
     */
    fun registerDietaryPreferencesUpdate() {
        repo.dietaryPreferencesUpdate.postValue(repo.dietaryPreferencesUpdate.value?.plus(1))
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
     * This value determines which canteen the user has selected.
     */
    var preferenceCanteen: Int
        get() = repo.getPreferenceCanteen()
        set(newValue) { repo.setPreferenceCanteen(newValue) }

    /**
     * This value determines whether the user has opened the canteen fragment before.
     */
    var preferenceHasOpenedCanteen: Boolean
        get() = repo.getPreferenceHasOpenedCanteen()
        set(newValue) { repo.setPreferenceHasOpenedCanteen(newValue) }

    /**
     * Determines which layout the user wants the app to use.
     */
    val preferenceAppLayout: AppLayout
        get() = repo.getAppLayout()

    /**
     * Determines in which format dates should be displayed on the canteen screen.
     */
    val preferenceCanteenDate: Int
        get() = repo.getPreferenceCanteenDate()

    fun getPreferenceFirstLaunch(): Boolean = repo.getPreferenceFirstLaunch()
    fun getPreferenceFeatureDate(): Boolean = repo.getPreferenceFeatureDate()
    fun setPreferenceFeatureDate(newValue: Boolean) { repo.setPreferenceFeatureDate(newValue) }
}