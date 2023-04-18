package com.denizk0461.studip.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.denizk0461.studip.model.*

@Dao
interface EventDAO {

    // Stud.IP schedule events
    @get:Query("SELECT * FROM events ORDER BY id")
    val allEvents: LiveData<List<StudIPEvent>>

    @Insert
    fun insertEvent(event: StudIPEvent)

    @Insert
    fun insertEvents(event: List<StudIPEvent>)

    @Query("DELETE FROM events")
    fun nukeEvents()

//    @get:Query("SELECT * FROM offers ORDER BY id")
//    val allOffers: LiveData<List<CanteenOffer>>
//
//    @Insert
//    fun insertOffers(offers: List<CanteenOffer>)
//
//    @Query("DELETE from offers")
//    fun nukeOffers()

    // canteen offers
    // get all offers
    @get:Query(
        "SELECT * FROM offer_item " +
                "JOIN offer_category ON offer_item.categoryId = offer_category.id " +
                "JOIN offer_canteen ON offer_category.canteenId = offer_canteen.id " +
                "JOIN offer_date ON offer_category.dateId = offer_date.id"
    )
    val allOffers: LiveData<List<CanteenOffer>>

    @Query(
        "SELECT * FROM offer_item " +
                "JOIN offer_category ON offer_item.categoryId = offer_category.id " +
                "JOIN offer_canteen ON offer_category.canteenId = offer_canteen.id " +
                "JOIN offer_date ON offer_category.dateId = offer_date.id " +
                "WHERE isFair = :isFair AND isFish = :isFish AND isPoultry = :isPoultry " +
                "AND isLamb = :isLamb AND isVital = :isVital AND isBeef = :isBeef " +
                "AND isPork = :isPork AND isVegan = :isVegan AND isVegetarian = :isVegetarian " +
                "AND isGame = :isGame "
    )
    fun getOffersByPreference(
        isFair: Boolean, isFish: Boolean, isPoultry: Boolean, isLamb: Boolean, isVital: Boolean,
        isBeef: Boolean, isPork: Boolean, isVegan: Boolean, isVegetarian: Boolean, isGame: Boolean
    ): LiveData<List<CanteenOffer>>

    @get:Query("SELECT id FROM offer_date")
    val updateObserver: LiveData<List<Int>>

    @Query("DELETE FROM offer_date")
    fun nukeOfferDates()

    @Query("DELETE FROM offer_category")
    fun nukeOfferCategories()

    @Query("DELETE FROM offer_canteen")
    fun nukeOfferCanteens()

    @Query("DELETE FROM offer_item")
    fun nukeOfferItems()

    @Insert fun insert(date: OfferDate)
    @Insert fun insert(canteen: OfferCanteen)
    @Insert fun insert(category: OfferCategory)
    @Insert fun insert(item: OfferItem)
}