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
//        when (pref) {
//            DietaryPreferences.FAIR -> oldString[0] = newValue.toChar()
//            DietaryPreferences.FISH -> oldString[1] = newValue.toChar()
//            DietaryPreferences.POULTRY -> oldString[2] = newValue.toChar()
//            DietaryPreferences.LAMB -> oldString[3] = newValue.toChar()
//            DietaryPreferences.VITAL -> oldString[4] = newValue.toChar()
//            DietaryPreferences.BEEF -> oldString[5] = newValue.toChar()
//            DietaryPreferences.PORK -> oldString[6] = newValue.toChar()
//            DietaryPreferences.VEGAN -> oldString[7] = newValue.toChar()
//            DietaryPreferences.VEGETARIAN -> oldString[8] = newValue.toChar()
//            DietaryPreferences.GAME -> oldString[9] = newValue.toChar()
//        }
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
    fun getPreference(pref: DietaryPreferences): Boolean =
        getDietaryPrefs()[pref.ordinal] == DietaryPrefObject.C_TRUE
//        val prefString = getDietaryPrefs().toList()
//        return when (pref) {
//            DietaryPreferences.FAIR -> prefString[0] == 't'
//            DietaryPreferences.FISH -> prefString[1] == 't'
//            DietaryPreferences.POULTRY -> prefString[2] == 't'
//            DietaryPreferences.LAMB -> prefString[3] == 't'
//            DietaryPreferences.VITAL -> prefString[4] == 't'
//            DietaryPreferences.BEEF -> prefString[5] == 't'
//            DietaryPreferences.PORK -> prefString[6] == 't'
//            DietaryPreferences.VEGAN -> prefString[7] == 't'
//            DietaryPreferences.VEGETARIAN -> prefString[8] == 't'
//            DietaryPreferences.GAME -> prefString[9] == 't'
//        }

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
}