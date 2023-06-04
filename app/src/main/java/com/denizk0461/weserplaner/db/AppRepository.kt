package com.denizk0461.weserplaner.db

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.denizk0461.weserplaner.values.DietaryPreferences
import com.denizk0461.weserplaner.values.SettingsPreferences
import com.denizk0461.weserplaner.values.TaskOrder
import com.denizk0461.weserplaner.model.*
import com.denizk0461.weserplaner.values.AllergenPreferences
import com.denizk0461.weserplaner.values.AppLayout

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
    private val blankDietaryPrefs: String = DietaryPreferences.TEMPLATE_EMPTY

    /**
     * Used to observe whenever updates are made to the dietary preferences.
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
                synchronized(Object::class.java) {
                    repo = AppRepository(app)
                }
            }
            return repo
        }
    }

    /**
     * Retrieve all Stud.IP events from the database.
     *
     * @return  all events
     */
    fun getAllEvents(): LiveData<List<StudIPEvent>> = dao.getAllEvents()

    /**
     * Retrieves the number of Stud.IP events available in the database.
     *
     * @return number of Stud.IP events
     */
    fun getEventCount(): LiveData<Int> = dao.getEventCount()

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
     * Retrieves tasks for all events ordered as specified.
     *
     * @param order order in which the tasks are returned
     * @return      tasks wrapped in a LiveData object
     */
    fun getTasks(order: TaskOrder): LiveData<List<EventTaskExtended>> = when (order) {
        TaskOrder.DATE_CREATED -> dao.getAllTasksOrderDateCreated()
        TaskOrder.ALPHABETICALLY -> dao.getAllTasksOrderAlphabetically()
        else -> dao.getAllTasksOrderDateDue() // TaskOrder.DATE_DUE
    }

    /**
     * Retrieves the canteen stored in the database. Should always be 0 or 1.
     *
     * @return canteen
     */
    fun getCanteen(): LiveData<OfferCanteen> = dao.getCanteen()

    /**
     * Retrieve opening hours for the fetched canteen.
     *
     * @return opening hours for the canteen
     */
    fun getCanteenOpeningHours(): LiveData<String> = dao.getCanteenOpeningHours()

    /**
     * Retrieve news for the fetched canteen.
     *
     * @return news for the canteen
     */
    fun getCanteenNews(): LiveData<String> = dao.getCanteenNews()

    /**
     * Retrieves all canteen offers that match given day.
     *
     * @return all canteen offers matching the given day exposed through a LiveData object
     */
    fun getOffersByDay(day: Int): LiveData<List<CanteenOffer>> = dao.getOffersByDay(day)

    /**
     * Inserts a new schedule element.
     *
     * @param event the event to save
     */
    fun insert(event: StudIPEvent) {
        dao.insert(event)
    }

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
    fun getDates(): LiveData<List<OfferDate>> = dao.getDates()

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

    // --- user-set preferences --- //

    /**
     * This value determines which allergens the user wants to have displayed or hidden.
     */
    fun getPreferenceAllergenConfig(): String = getStringPreference(
        SettingsPreferences.ALLERGEN_CONFIG,
        defaultValue = AllergenPreferences.TEMPLATE,
    )
    /**
     * This value determines how many allergens the user wants to have hidden.
     */
    fun getPreferenceAllergenConfigCount(): Int = getPreferenceAllergenConfig().run {
        if (isBlank()) 0 else split(",").count()
    }
    fun setPreferenceAllergenConfig(newValue: String) {
        setPreference(SettingsPreferences.ALLERGEN_CONFIG, newValue)
    }

    /**
     * This value determines whether the user wants to have allergens marked.
     */
    fun getPreferenceAllergen(): Boolean = getBooleanPreference(
        SettingsPreferences.ALLERGEN,
        defaultValue = true,
    )
    fun setPreferenceAllergen(newValue: Boolean) {
        setPreference(SettingsPreferences.ALLERGEN, newValue)
    }

    /**
     * This value determines which canteen the user has selected.
     */
    fun getPreferenceColour(): Boolean = getBooleanPreference(
        SettingsPreferences.COLOUR_PREFS,
        defaultValue = true,
    )
    fun setPreferenceColour(newValue: Boolean) {
        setPreference(SettingsPreferences.COLOUR_PREFS, newValue)
    }

    /**
     * This value determines which canteen the user has selected.
     */
    fun getPreferenceCanteen(): Int = getIntPreference(
        SettingsPreferences.CANTEEN,
    )
    fun setPreferenceCanteen(newValue: Int) {
        setPreference(SettingsPreferences.CANTEEN, newValue)
    }

    /**
     * This value determines whether the user has opened the canteen fragment before.
     */
    fun getPreferenceHasOpenedCanteen(): Boolean = getBooleanPreference(
        SettingsPreferences.HAS_OPENED_CANTEEN,
    )
    fun setPreferenceHasOpenedCanteen(newValue: Boolean) {
        setPreference(SettingsPreferences.HAS_OPENED_CANTEEN, newValue)
    }

    /**
     * This value determines whether the user wants to have the next course in their schedule
     * highlighted.
     */
    fun getPreferenceCourseHighlighting(): Boolean = getBooleanPreference(
            SettingsPreferences.COURSE_HIGHLIGHTING,
            defaultValue = true,
        )
    fun setPreferenceCourseHighlighting(newValue: Boolean) {
        setPreference(SettingsPreferences.COURSE_HIGHLIGHTING, newValue)
    }

    /**
     * This value determines whether the user wants their timetable to launch with the current day.
     */
    fun getPreferenceCurrentDay(): Boolean = getBooleanPreference(
            SettingsPreferences.CURRENT_DAY,
            defaultValue = true,
        )
    fun setPreferenceCurrentDay(newValue: Boolean) {
        setPreference(SettingsPreferences.CURRENT_DAY, newValue)
    }

    /**
     * Determines whether the user is launching the app for the first time.
     */
    fun getPreferenceFirstLaunch(): Boolean = getBooleanPreference(
            SettingsPreferences.FIRST_LAUNCH,
            defaultValue = true,
        )
    fun setPreferenceFirstLaunch(newValue: Boolean) {
        setPreference(SettingsPreferences.FIRST_LAUNCH, newValue)
    }

    /**
     * This value determines which fragment the user wants the app to start with. Order is equal to
     * the order that the items are arranged in in the bottom nav bar.
     */
    fun getPreferenceLaunchFragment(): Int = getIntPreference(
            SettingsPreferences.LAUNCH_FRAGMENT_ON_START,
        )
    fun setPreferenceLaunchFragment(newValue: Int) {
        setPreference(SettingsPreferences.LAUNCH_FRAGMENT_ON_START, newValue)
    }

    /**
     * This value determines which fragment the user wants the app to start with. Order is equal to
     * the order that the items are arranged in in the bottom nav bar.
     */
    fun getPreferencePricing(): Int = getIntPreference(
        SettingsPreferences.PRICING,
    )
    fun setPreferencePricing(newValue: Int) {
        setPreference(SettingsPreferences.PRICING, newValue)
    }

    /**
     * This value determines which navigation graph will be used for app navigation.
     */
    fun getAppLayout(): AppLayout = AppLayout.values()[getIntPreference(
        SettingsPreferences.APP_LAYOUT,
    )]
    fun setAppLayout(newValue: Int) {
        setPreference(SettingsPreferences.APP_LAYOUT, newValue)
    }
    fun setAppLayout(newValue: AppLayout) {
        setPreference(SettingsPreferences.APP_LAYOUT, newValue.ordinal)
    }

    /**
     * This value determines whether the user opts into submitting crash reports.
     */
    fun getPreferenceDataHandling(): Boolean = getBooleanPreference(
        SettingsPreferences.DATA_HANDLING,
    )
    fun setPreferenceDataHandling(newValue: Boolean) {
        setPreference(SettingsPreferences.DATA_HANDLING, newValue)
    }

    /**
     * This value determines whether the user has enabled experimental settings.
     */
    fun getPreferenceExperimentalSettingsEnabled(): Boolean = getBooleanPreference(
        SettingsPreferences.EXPERIMENTAL_ENABLED,
    )
    fun setPreferenceExperimentalSettingsEnabled(newValue: Boolean) {
        setPreference(SettingsPreferences.EXPERIMENTAL_ENABLED, newValue)
    }

    /**
     * This value determines whether beta screens should be shown.
     */
    fun getPreferenceBetaScreensEnabled(): Boolean = getBooleanPreference(
        SettingsPreferences.BETA_SCREENS_ENABLED,
    )
    fun setPreferenceBetaScreensEnabled(newValue: Boolean) {
        setPreference(SettingsPreferences.BETA_SCREENS_ENABLED, newValue)
    }

    // --- private preference handlers preferences --- //

    /**
     * Retrieves a user-set boolean preference.
     *
     * @param pref  preference to retrieve
     * @return      whether the user set this preference
     */
    private fun getBooleanPreference(pref: SettingsPreferences, defaultValue: Boolean = false): Boolean =
        prefs.getBoolean(pref.key, defaultValue)

    /**
     * Updates a user-set boolean preference.
     *
     * @param pref      preference to set
     * @param newValue  new value to set the preference to
     */
    private fun setPreference(pref: SettingsPreferences, newValue: Boolean) {
        prefs.edit().putBoolean(pref.key, newValue).apply()
    }

    /**
     * Retrieves a user-set string preference.
     *
     * @param pref  preference to retrieve
     * @return      string that was set
     */
    private fun getStringPreference(pref: SettingsPreferences, defaultValue: String = ""): String =
        prefs.getString(pref.key, defaultValue) ?: ""

    /**
     * Updates a user-set string preference.
     *
     * @param pref      preference to set
     * @param newValue  new value to set the preference to
     */
    private fun setPreference(pref: SettingsPreferences, newValue: String) {
        prefs.edit().putString(pref.key, newValue).apply()
    }

    /**
     * Retrieves a user-set integer preference.
     *
     * @param pref  preference to retrieve
     * @return      whether the user set this preference
     */
    private fun getIntPreference(pref: SettingsPreferences): Int = prefs.getInt(pref.key, 0)

    /**
     * Updates a user-set integer preference.
     *
     * @param pref      preference to set
     * @param newValue  new value to set the preference to
     */
    private fun setPreference(pref: SettingsPreferences, newValue: Int) {
        prefs.edit().putInt(pref.key, newValue).apply()
    }
}