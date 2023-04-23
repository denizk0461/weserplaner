package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.data.StwParser
import com.denizk0461.studip.model.CanteenOffer
import com.denizk0461.studip.model.DietaryPrefObject
import com.denizk0461.studip.model.DietaryPreferences
import com.denizk0461.studip.model.OfferDate

/**
 * View model for [com.denizk0461.studip.fragment.CanteenFragment]
 *
 * @param app   reference to the app
 */
class CanteenViewModel(app: Application) : TemplateViewModel(app) {

    // Instantiate a parser, should the user want to refresh the canteen offers
    private val parser = StwParser()

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
    fun getDietaryPrefs(): DietaryPrefObject = repo.getDietaryPrefsAsObj()

    /**
     * Retrieves all canteen offer date objects.
     *
     * @return a list of instances of canteen offer dates
     */
    fun getDates(): List<OfferDate> = returnBlocking { repo.getDates() }

    // Fetch the canteen offers asynchronously. Updates will be provided through a LiveData object.
    fun fetchOffers(onRefreshUpdate: (status: Int) -> Unit, onFinish: () -> Unit) {
        doAsync { parser.parse(onRefreshUpdate, onFinish) }
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
    fun getPreference(pref: DietaryPreferences): Boolean = repo.getPreference(pref)
}