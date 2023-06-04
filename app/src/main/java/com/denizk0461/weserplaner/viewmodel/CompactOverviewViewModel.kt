package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.denizk0461.weserplaner.data.StwParser
import com.denizk0461.weserplaner.model.CanteenOffer
import com.denizk0461.weserplaner.model.OfferCanteen
import com.denizk0461.weserplaner.values.DietaryPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompactOverviewViewModel(application: Application) : AppViewModel(application) {

    // Instantiate a parser, should the user want to refresh the canteen offers
    private val parser = StwParser(application)

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
     * Retrieves all canteen offers that match given day.
     *
     * @return all canteen offers matching the given day exposed through a LiveData object
     */
    fun getOffersByDay(day: Int): LiveData<List<CanteenOffer>> =
        repo.getOffersByDay(day)

    /**
     * Retrieves the canteen stored in the database. Should always be 0 or 1.
     *
     * @return canteen
     */
    fun getCanteen(): LiveData<OfferCanteen> = repo.getCanteen()

    /**
     * This value determines which canteen the user has selected.
     */
    val preferenceCanteen: Int
        get() = repo.getPreferenceCanteen()

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
        get() = repo.getPreferenceAllergenConfig()

    /**
     * This value determines whether the user wants to have allergens marked.
     */
    val preferenceAllergen: Boolean
        get() = repo.getPreferenceAllergen()

    /**
     * This value determines which canteen the user has selected.
     */
    val preferenceColour: Boolean
        get() = repo.getPreferenceColour()
}