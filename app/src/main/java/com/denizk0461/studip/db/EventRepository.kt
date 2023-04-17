package com.denizk0461.studip.db

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.studip.model.*

class EventRepository(app: Application) {

    private val dao: EventDAO = EventDatabase.getInstance(app.applicationContext).dao()

    val allEvents: LiveData<List<StudIPEvent>> = dao.allEvents

    fun insertEvent(event: StudIPEvent) { dao.insertEvent(event) }
    fun insertEvents(events: List<StudIPEvent>) { dao.insertEvents(events) }
    fun nukeEvents() { dao.nukeEvents() }

    val allOffers: LiveData<List<CanteenOffer>> = dao.allOffers

//    fun insertOffers(offers: List<CanteenOffer>) { dao.insertOffers(offers) }
    fun nukeOffers() {
        dao.nukeOfferItems()
        dao.nukeOfferCategories()
        dao.nukeOfferCanteens()
        dao.nukeOfferDates()
    }

    fun insert(date: OfferDate) { dao.insert(date) }
    fun insert(canteen: OfferCanteen) { dao.insert(canteen) }
    fun insert(category: OfferCategory) { dao.insert(category) }
    fun insert(item: OfferItem) { dao.insert(item) }
}