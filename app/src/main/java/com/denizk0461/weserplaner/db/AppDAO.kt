package com.denizk0461.weserplaner.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.denizk0461.weserplaner.model.*

/**
 * Database access object used for all database transactions in the app. Can be accessed through an
 * instance of AppRepository.kt.
 */
@Dao
interface AppDAO {

    /* --- Stud.IP schedule events --- */

    /**
     * Retrieves the number of Stud.IP events available in the database.
     *
     * @return number of Stud.IP events
     */
    @Query("SELECT COUNT(*) FROM studip_events ORDER BY timeslotId, eventId")
    fun getEventCount(): LiveData<Int>

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

    /* --- event tasks --- */

    /**
     * Retrieve all event tasks ordered by their ID (order: date created).
     *
     * @return  tasks ordered by date created
     */
    @Query(
        "SELECT event_tasks.taskId, " +
                "event_tasks.eventId, " +
                "event_tasks.dueDate, " +
                "event_tasks.title AS taskTitle, " +
                "studip_events.title AS eventTitle, " +
                "studip_events.lecturer, " +
                "event_tasks.notes, " +
                "studip_events.room " +
                "FROM event_tasks " +
                "JOIN studip_events ON event_tasks.eventId = studip_events.eventId " +
                "ORDER BY taskId"
    )
    fun getAllTasksOrderDateCreated(): LiveData<List<EventTaskExtended>>

    /**
     * Retrieve all event tasks ordered alphabetically by their title.
     *
     * @return  tasks ordered by title
     */
    @Query(
        "SELECT event_tasks.taskId, " +
                "event_tasks.eventId, " +
                "event_tasks.dueDate, " +
                "event_tasks.title AS taskTitle, " +
                "studip_events.title AS eventTitle, " +
                "studip_events.lecturer, " +
                "event_tasks.notes, " +
                "studip_events.room " +
                "FROM event_tasks " +
                "JOIN studip_events ON event_tasks.eventId = studip_events.eventId " +
            "ORDER BY taskTitle")
    fun getAllTasksOrderAlphabetically(): LiveData<List<EventTaskExtended>>

    /**
     * Retrieve all event tasks ordered by their due date.
     *
     * @return  tasks ordered by date due
     */
    @Query(
        "SELECT event_tasks.taskId, " +
                "event_tasks.eventId, " +
                "event_tasks.dueDate, " +
                "event_tasks.title AS taskTitle, " +
                "studip_events.title AS eventTitle, " +
                "studip_events.lecturer, " +
                "event_tasks.notes, " +
                "studip_events.room " +
                "FROM event_tasks " +
                "JOIN studip_events ON event_tasks.eventId = studip_events.eventId " +
            "ORDER BY dueDate")
    fun getAllTasksOrderDateDue(): LiveData<List<EventTaskExtended>>

    /* --- canteen offers --- */

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
     * Retrieves all canteen offers that match given day. Objects will be joined through their
     * primary/foreign keys from instances of OfferDate.kt, OfferCanteen.kt, OfferCategory.kt, and
     * OfferItem.kt.
     *
     * @return all canteen offers matching the given day exposed through a LiveData object
     */
    @Query(
        "SELECT * FROM offer_item " +
                "JOIN offer_category ON offer_item.categoryId = offer_category.id " +
                "JOIN offer_canteen ON offer_category.canteenId = offer_canteen.id " +
                "JOIN offer_date ON offer_category.dateId = offer_date.id " +
                "WHERE dateId = :day "
    )
    fun getOffersByDay(day: Int): LiveData<List<CanteenOffer>>

    /**
     * Retrieves all canteen offer date objects.
     *
     * @return a list of instances of canteen offer dates
     */
    @Query("SELECT * FROM offer_date ORDER BY id")
    fun getDates(): LiveData<List<OfferDate>>

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
     * Retrieves the canteen stored in the database. Should always be 0 or 1.
     *
     * @return canteen
     */
    @Query("SELECT * FROM offer_canteen LIMIT 1")
    fun getCanteen(): LiveData<OfferCanteen>

    /**
     * Retrieve opening hours for the fetched canteen.
     *
     * @return opening hours for the canteen
     */
    @Query("SELECT openingHours FROM offer_canteen LIMIT 1")
    fun getCanteenOpeningHours(): LiveData<String>

    /**
     * Retrieve news for the fetched canteen.
     *
     * @return news for the canteen
     */
    @Query("SELECT news FROM offer_canteen LIMIT 1")
    fun getCanteenNews(): LiveData<String>
}