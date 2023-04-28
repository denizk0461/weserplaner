package com.denizk0461.studip.db

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val blankDietaryPrefs: String = DietaryPreferences.C_FALSE.toString().repeat(10)

    /**
     * Used to observe just to register an update of the user-set dietary preferences.
     */
    val dietaryPreferencesUpdate: MutableLiveData<Int> = MutableLiveData(0)

    companion object {
        /**
         * Reference to the app's repository. It is only instantiated once because it can only be
         * accessed by [getRepositoryInstance].
         */
        private lateinit var repo: AppRepository

        /**
         * Get a static instance of the repository. If none has been instantiated, one will be created
         * and saved into [repo].
         *
         * @return  static instance of the repository
         */
        fun getRepositoryInstance(app: Application): AppRepository {
            if (!::repo.isInitialized) {
                repo = AppRepository(app)
            }
            return repo
        }
    }

    /**
     * Retrieves Stud.IP events for a specific day, ordered by their timeslots, then their IDs.
     *
     * @return  Stud.IP events for a certain day exposed through a LiveData object
     */
    fun getEventsForDay(day: Int): LiveData<List<StudIPEvent>> = dao.getEventsForDay(day)

    /**
     * Inserts a list of Stud.IP events into the database.
     *
     * @param events objects to be saved to the database
     */
    fun insertEvents(events: List<StudIPEvent>) { dao.insertEvents(events) }

    /**
     * Deletes all Stud.IP events from the database.
     */
    fun nukeEvents() { dao.nukeEvents() }

    /**
     * Retrieves the opening hours of the canteen as a string.
     *
     * @return  opening hours
     */
    fun getCanteenOpeningHours(): String = dao.getCanteenOpeningHours()

    /**
     * Retrieves all canteen offers.
     *
     * @return all canteen offers exposed through a LiveData object
     */
    val allOffers: LiveData<List<CanteenOffer>> = dao.allOffers

    /**
     * Retrieves all canteen offers that match given day.
     *
     * @return all canteen offers matching the given day exposed through a LiveData object
     */
    fun getOffersByDay(day: Int): LiveData<List<CanteenOffer>> = dao.getOffersByDay(day)

    /**
     * Retrieves the amount of dates represented in the offers stored locally. Can be observed.
     *
     * @return  date count as LiveData
     */
    fun getDateCount(): LiveData<Int> = dao.getDateCount()

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
    private fun getDietaryPrefsAsString(): String {
        return prefs.getString(dietaryPrefString, blankDietaryPrefs) ?: blankDietaryPrefs
    }

    /**
     * Retrieve the user's dietary preferences as a [DietaryPreferences.Object].
     *
     * @return dietary preferences as an instance of [DietaryPreferences.Object]
     */
    fun getDietaryPrefsAsObject(): DietaryPreferences.Object =
        DietaryPreferences.construct(getDietaryPrefsAsString())

    /**
     * Updates a given dietary preference to a new value.
     *
     * @param pref      preference to be updated
     * @param newValue  value to set the preference to
     */
    fun setPreference(pref: DietaryPreferences, newValue: Boolean) {
        val oldString = getDietaryPrefsAsString().toMutableList()

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
    private fun Boolean.toChar() = if (this) {
        DietaryPreferences.C_TRUE
    } else {
        DietaryPreferences.C_FALSE
    }

    /**
     * Retrieves a single user-specified dietary preference.
     *
     * @param pref  the dietary preference that should be retrieved
     * @return      whether the preference needs to be met1
     */
    fun getBooleanPreference(pref: DietaryPreferences): Boolean =
        getDietaryPrefsAsString()[pref.ordinal] == DietaryPreferences.C_TRUE

    /**
     * Deletes all canteen offers from the database.
     */
    fun nukeOffers() {
        nukeOfferItems()
        nukeOfferCategories()
        nukeOfferCanteens()
        nukeOfferDates()
    }

    /**
     * Deletes all canteen items from the database.
     */
    fun nukeOfferItems() { dao.nukeOfferItems() }

    /**
     * Deletes all canteen categories from the database.
     */
    fun nukeOfferCategories() { dao.nukeOfferCategories() }

    /**
     * Deletes all canteens from the database.
     */
    fun nukeOfferCanteens() { dao.nukeOfferCanteens() }

    /**
     * Deletes all canteen dates from the database.
     */
    fun nukeOfferDates() { dao.nukeOfferDates() }

    /**
     * Inserts a list of dates into the database.
     *
     * @param dates objects to be saved to the database
     */
    fun insertDates(dates: List<OfferDate>) { dao.insertDates(dates) }

    /**
     * Inserts a list of canteens into the database.
     *
     * @param canteens  objects to be saved to the database
     */
    fun insertCanteens(canteens: List<OfferCanteen>) { dao.insertCanteens(canteens) }

    /**
     * Inserts a list of categories into the database.
     *
     * @param categories    objects to be saved to the database
     */
    fun insertCategories(categories: List<OfferCategory>) { dao.insertCategories(categories) }

    /**
     * Inserts a list of items into the database.
     *
     * @param items objects to be saved to the database
     */
    fun insertItems(items: List<OfferItem>) { dao.insertItems(items) }

    // --- settings preferences --- //

    /**
     * Retrieves ta user-set boolean preference.
     *
     * @param pref  preference to retrieve
     * @return      whether the user set this preference
     */
    fun getBooleanPreference(pref: SettingsPreferences, defaultValue: Boolean = false): Boolean =
        prefs.getBoolean(pref.key, defaultValue)

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