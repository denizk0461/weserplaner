package com.denizk0461.studip.db

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.denizk0461.studip.model.*

/**
 * Repository objects that acts as a mediator between view models and the database to retrieve data
 * without being bound to a view lifecycle.
 *
 * @param app reference to the application
 */
class AppRepository(app: Application) {

    // Reference to the database access object used to handle database transactions
    private val dao: AppDAO = AppDatabase.getInstance(app.applicationContext).dao()

    // Shared preferences used to store small values
    private val prefs = PreferenceManager.getDefaultSharedPreferences(app)

    // Tag used to store the user's dietary preferences in persistent storage (shared preferences)
    private val dietaryPrefString = "prefs_obj"

    // Blank dietary preference regular expression
    private val blankDietaryPrefs: String = DietaryPrefObject.C_FALSE.toString().repeat(10)

    /**
     * Retrieves all Stud.IP events.
     *
     * @return all Stud.IP events exposed through a LiveData object
     */
    val allEvents: LiveData<List<StudIPEvent>> = dao.allEvents

    /**
     * Inserts a list of Stud.IP events into the database.
     *
     * @param events objects to be saved to the database
     */
    fun insertEvents(events: List<StudIPEvent>) { dao.insert(events) }

    /**
     * Deletes all Stud.IP events from the database.
     */
    fun nukeEvents() { dao.nukeEvents() }

    /**
     * Retrieves all canteen offers.
     *
     * @return all canteen offers exposed through a LiveData object
     */
    val allOffers: LiveData<List<CanteenOffer>> = dao.allOffers

    /**
     * Updates a schedule element.
     *
     * @param event the event to update
     */
    fun update(event: StudIPEvent) {
        dao.update(event)
    }

    /**
     * Deletes a schedule element.
     *
     * @param event the event to delete
     */
    fun delete(event: StudIPEvent) {
        dao.delete(event)
    }

    /**
     * Retrieves all canteen offer date objects.
     *
     * @return a list of instances of canteen offer dates
     */
    fun getDates(): List<OfferDate> = dao.getDates()

    /**
     * Retrieves the regex string used to determine the user's dietary preferences, or, if no
     * preference has been set, a 'blank' expression denoting such.
     *
     * @return dietary preferences as a regular expression string
     */
    private fun getDietaryPrefs(): String {
        return prefs.getString(dietaryPrefString, blankDietaryPrefs) ?: blankDietaryPrefs
    }

    /**
     * Retrieve the user's dietary preferences as a DietaryPrefObject.
     *
     * @return dietary preferences as an instance of DietaryPrefObject.kt
     */
    fun getDietaryPrefsAsObj(): DietaryPrefObject = DietaryPrefObject.construct(getDietaryPrefs())

    /**
     * Updates a given dietary preference to a new value.
     *
     * @param pref      preference to be updated
     * @param newValue  value to set the preference to
     */
    fun setPreference(pref: DietaryPreferences, newValue: Boolean) {
        val oldString = getDietaryPrefs().toMutableList()

        oldString[pref.ordinal] = newValue.toChar()
        val newString = oldString.joinToString("")
        prefs.edit().putString(dietaryPrefString, newString).apply()
    }

    /**
     * Converts a boolean value to the corresponding value used to construct a regular expression
     * for determining whether a dietary preference is met.
     *
     * @return char denoting whether a preference needs to be met
     */
    private fun Boolean.toChar() = if (this) DietaryPrefObject.C_TRUE else DietaryPrefObject.C_FALSE

    /**
     * Retrieves a single user-specified dietary preference.
     *
     * @param pref  the dietary preference that should be retrieved
     * @return      whether the preference needs to be met1
     */
    fun getBooleanPreference(pref: DietaryPreferences): Boolean =
        getDietaryPrefs()[pref.ordinal] == DietaryPrefObject.C_TRUE

    /**
     * Deletes all canteen offers from the database.
     */
    fun nukeOffers() {
        dao.nukeOfferItems()
        dao.nukeOfferCategories()
        dao.nukeOfferCanteens()
        dao.nukeOfferDates()
    }

    /**
     * Inserts a date into the database.
     *
     * @param date object to be saved to the database
     */
    fun insert(date: OfferDate) { dao.insert(date) }

    /**
     * Inserts a canteen into the database.
     *
     * @param canteen object to be saved to the database
     */
    fun insert(canteen: OfferCanteen) { dao.insert(canteen) }

    /**
     * Inserts a canteen offer category into the database.
     *
     * @param category object to be saved to the database
     */
    fun insert(category: OfferCategory) { dao.insert(category) }

    /**
     * Inserts a canteen offer item into the database.
     *
     * @param item object to be saved to the database
     */
    fun insert(item: OfferItem) { dao.insert(item) }

    // --- settings preferences --- //

    /**
     * Retrieves ta user-set boolean preference.
     *
     * @param pref  preference to retrieve
     * @return      whether the user set this preference
     */
    fun getBooleanPreference(pref: SettingsPreferences): Boolean = prefs.getBoolean(pref.key, false)

    /**
     * Retrieves ta user-set integer preference.
     *
     * @param pref  preference to retrieve
     * @return      whether the user set this preference
     */
    fun getIntPreference(pref: SettingsPreferences): Int = prefs.getInt(pref.key, 0)

    /**
     * Updates a user-set boolean preference.
     *
     * @param pref      preference to set
     * @param newValue  new value to set the preference to
     */
    fun setPreference(pref: SettingsPreferences, newValue: Boolean) {
        prefs.edit().putBoolean(pref.key, newValue).apply()
    }

    /**
     * Updates a user-set integer preference.
     *
     * @param pref      preference to set
     * @param newValue  new value to set the preference to
     */
    fun setPreference(pref: SettingsPreferences, newValue: Int) {
        prefs.edit().putInt(pref.key, newValue).apply()
    }
}