package com.denizk0461.studip.db

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.denizk0461.studip.model.*

class EventRepository(private val app: Application) {

    private val dao: EventDAO = EventDatabase.getInstance(app.applicationContext).dao()
    private val prefs = PreferenceManager.getDefaultSharedPreferences(app)

    val allEvents: LiveData<List<StudIPEvent>> = dao.allEvents

    fun insertEvent(event: StudIPEvent) { dao.insertEvent(event) }
    fun insertEvents(events: List<StudIPEvent>) { dao.insertEvents(events) }
    fun nukeEvents() { dao.nukeEvents() }

    val allOffers: LiveData<List<CanteenOffer>> = dao.allOffers

    // is this better than List.filter?
    fun getOffersByPreference(prefs: DietaryPrefObject = getDietaryPrefs()): LiveData<List<CanteenOffer>> {
        return if (isPreferenceSet()) {
            dao.getOffersByPreference(
                isFair = prefs.isFair,
                isFish = prefs.isFish,
                isPoultry = prefs.isPoultry,
                isLamb = prefs.isLamb,
                isVital = prefs.isVital,
                isBeef = prefs.isBeef,
                isPork = prefs.isPork,
                isVegan = prefs.isVegan,
                isVegetarian = prefs.isVegetarian,
                isGame = prefs.isGame,
            )
        } else {
            dao.allOffers
        }
    }


    private fun getDietaryPrefs(): DietaryPrefObject {
        return DietaryPrefObject(
            isFair = prefs.getBoolean(DietaryPreferences.FAIR.value, false),
            isFish = prefs.getBoolean(DietaryPreferences.FISH.value, false),
            isPoultry = prefs.getBoolean(DietaryPreferences.POULTRY.value, false),
            isLamb = prefs.getBoolean(DietaryPreferences.LAMB.value, false),
            isVital = prefs.getBoolean(DietaryPreferences.VITAL.value, false),
            isBeef = prefs.getBoolean(DietaryPreferences.BEEF.value, false),
            isPork = prefs.getBoolean(DietaryPreferences.PORK.value, false),
            isVegan = prefs.getBoolean(DietaryPreferences.VEGAN.value, false),
            isVegetarian = prefs.getBoolean(DietaryPreferences.VEGETARIAN.value, false),
            isGame = prefs.getBoolean(DietaryPreferences.GAME.value, false),
        )
    }

    private fun isPreferenceSet(): Boolean {
        getDietaryPrefs().also { p ->
            if (p.isFair) return true
            if (p.isFish) return true
            if (p.isPoultry) return true
            if (p.isLamb) return true
            if (p.isVital) return true
            if (p.isBeef) return true
            if (p.isPork) return true
            if (p.isVegan) return true
            if (p.isVegetarian) return true
            if (p.isGame) return true
        }
        return false
    }

    fun setPreference(pref: DietaryPreferences, newValue: Boolean) {
        prefs.edit().putBoolean(pref.value, newValue).apply()
    }

    fun getPreference(pref: DietaryPreferences): Boolean =
        prefs.getBoolean(pref.value, false)

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