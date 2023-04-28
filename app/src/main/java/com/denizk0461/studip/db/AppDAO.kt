package com.denizk0461.studip.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denizk0461.studip.model.*

/**
 * Database access object used for all database transactions in the app. Can be accessed through an
 * instance of AppRepository.kt.
 */
@Dao
interface AppDAO {

    /* --- Stud.IP schedule events --- */

    /**
     * Retrieves all Stud.IP events, ordered by their timeslots, then their IDs.
     *
     * @return all Stud.IP events exposed through a LiveData object
     */
    @get:Query("SELECT * FROM studip_events ORDER BY timeslotId, eventId")
    val allEvents: LiveData<List<StudIPEvent>>

    /**
     * Retrieves Stud.IP events for a specific day, ordered by their timeslots, then their IDs.
     *
     * @return  Stud.IP events for a certain day exposed through a LiveData object
     */
    @Query("SELECT * FROM studip_events WHERE day = :day ORDER BY timeslotId, eventId")
    fun getEventsForDay(day: Int): LiveData<List<StudIPEvent>>

    /**
     * Updates a given Stud.IP element.
     *
     * @param event object to be updated
     */
    @Update
    fun update(event: StudIPEvent)

    /**
     * Updates a given Stud.IP element.
     *
     * @param event object to be updated
     */
    @Delete
    fun delete(event: StudIPEvent)

    /**
     * Inserts a Stud.IP event into the database.
     *
     * @param event object to be saved to the database
     */
    @Insert fun insert(event: StudIPEvent)

    /**
     * Inserts a list of Stud.IP events into the database.
     *
     * @param events objects to be saved to the database
     */
    @Insert fun insertEvents(events: List<StudIPEvent>)

    /**
     * Deletes all Stud.IP events from the database.
     */
    @Query("DELETE FROM studip_events")
    fun nukeEvents()

    /* --- canteen offers --- */

    /**
     * Retrieves all canteen offers. Objects will be joined through their primary/foreign keys from
     * instances of OfferDate.kt, OfferCanteen.kt, OfferCategory.kt, and OfferItem.kt.
     *
     * @return all canteen offers exposed through a LiveData object
     */
    @get:Query(
        "SELECT * FROM offer_item " +
                "JOIN offer_category ON offer_item.categoryId = offer_category.id " +
                "JOIN offer_canteen ON offer_category.canteenId = offer_canteen.id " +
                "JOIN offer_date ON offer_category.dateId = offer_date.id"
    )
    val allOffers: LiveData<List<CanteenOffer>>

    /**
     * Retrieves all canteen offers that match given dietary preferences. Objects will be joined
     * through their primary/foreign keys from instances of OfferDate.kt, OfferCanteen.kt,
     * OfferCategory.kt, and OfferItem.kt.
     *
     * @return all canteen offers matching the given preference exposed through a LiveData object
     */
    @Query(
        "SELECT * FROM offer_item " +
                "JOIN offer_category ON offer_item.categoryId = offer_category.id " +
                "JOIN offer_canteen ON offer_category.canteenId = offer_canteen.id " +
                "JOIN offer_date ON offer_category.dateId = offer_date.id " +
                "WHERE dietary_preferences = :prefs "
    )
    fun getOffersByPreference(prefs: String): LiveData<List<CanteenOffer>>

    /**
     * Retrieves all canteen offer date objects.
     *
     * @return a list of instances of canteen offer dates
     */
    @Query("SELECT * FROM offer_date ORDER BY id")
    fun getDates(): List<OfferDate>

    /**
     * Deletes all dates from the database.
     */
    @Query("DELETE FROM offer_date")
    fun nukeOfferDates()

    /**
     * Deletes all canteens from the database.
     */
    @Query("DELETE FROM offer_canteen")
    fun nukeOfferCanteens()

    /**
     * Deletes all canteen offer categories from the database.
     */
    @Query("DELETE FROM offer_category")
    fun nukeOfferCategories()

    /**
     * Deletes all canteen offer items from the database.
     */
    @Query("DELETE FROM offer_item")
    fun nukeOfferItems()

    /**
     * Inserts a date into the database.
     *
     * @param date  object to be saved to the database
     */
    @Insert fun insert(date: OfferDate)

    /**
     * Inserts a list of dates into the database.
     *
     * @param dates objects to be saved to the database
     */
    @Insert fun insertDates(dates: List<OfferDate>)

    /**
     * Inserts a canteen into the database.
     *
     * @param canteen   object to be saved to the database
     */
    @Insert fun insert(canteen: OfferCanteen)

    /**
     * Inserts a list of canteens into the database.
     *
     * @param canteens  objects to be saved to the database
     */
    @Insert fun insertCanteens(canteens: List<OfferCanteen>)

    /**
     * Inserts a canteen offer category into the database.
     *
     * @param category object to be saved to the database
     */
    @Insert fun insert(category: OfferCategory)

    /**
     * Inserts a list of categories into the database.
     *
     * @param categories    objects to be saved to the database
     */
    @Insert fun insertCategories(categories: List<OfferCategory>)

    /**
     * Inserts a canteen offer item into the database.
     *
     * @param item object to be saved to the database
     */
    @Insert fun insert(item: OfferItem)

    /**
     * Inserts a list of items into the database.
     *
     * @param items objects to be saved to the database
     */
    @Insert fun insertItems(items: List<OfferItem>)

    /**
     * Retrieves the opening hours of the canteen as a string.
     *
     * @return  opening hours
     */
    @Query("SELECT openingHours FROM offer_canteen LIMIT 1")
    fun getCanteenOpeningHours(): String
}