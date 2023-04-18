package com.denizk0461.studip.db

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.denizk0461.studip.model.*

class EventRepository(private val app: Application) {

    private val dao: EventDAO = EventDatabase.getInstance(app.applicationContext).dao()
    private val prefs = PreferenceManager.getDefaultSharedPreferences(app)

    val allEvents: LiveData<List<StudIPEvent>> = dao.allEvents

    private val dietaryPrefString = "prefs_obj"

    fun insertEvent(event: StudIPEvent) { dao.insertEvent(event) }
    fun insertEvents(events: List<StudIPEvent>) { dao.insertEvents(events) }
    fun nukeEvents() { dao.nukeEvents() }

    val allOffers: LiveData<List<CanteenOffer>> = dao.allOffers

    // is this better than List.filter?
    fun getOffersByPreference(prefs: String = getDietaryPrefs()): LiveData<List<CanteenOffer>> {
        return if (prefs != "ffffffffff") {//isPreferenceSet()) {
            dao.getOffersByPreference(
                prefs
//                isFair = prefs.isFair,
//                isFish = prefs.isFish,
//                isPoultry = prefs.isPoultry,
//                isLamb = prefs.isLamb,
//                isVital = prefs.isVital,
//                isBeef = prefs.isBeef,
//                isPork = prefs.isPork,
//                isVegan = prefs.isVegan,
//                isVegetarian = prefs.isVegetarian,
//                isGame = prefs.isGame,
            )
        } else {
            dao.allOffers
        }
//        return dao.allOffers
    }


    fun getDietaryPrefs(): String {
        return prefs.getString(dietaryPrefString, "ffffffffff") ?: "ffffffffff"
//        return DietaryPrefObject(
//            isFair = prefs.getBoolean(DietaryPreferences.FAIR.value, false),
//            isFish = prefs.getBoolean(DietaryPreferences.FISH.value, false),
//            isPoultry = prefs.getBoolean(DietaryPreferences.POULTRY.value, false),
//            isLamb = prefs.getBoolean(DietaryPreferences.LAMB.value, false),
//            isVital = prefs.getBoolean(DietaryPreferences.VITAL.value, false),
//            isBeef = prefs.getBoolean(DietaryPreferences.BEEF.value, false),
//            isPork = prefs.getBoolean(DietaryPreferences.PORK.value, false),
//            isVegan = prefs.getBoolean(DietaryPreferences.VEGAN.value, false),
//            isVegetarian = prefs.getBoolean(DietaryPreferences.VEGETARIAN.value, false),
//            isGame = prefs.getBoolean(DietaryPreferences.GAME.value, false),
//        )
    }

    fun getDietaryPrefsAsObj(): DietaryPrefObject = DietaryPrefObject.construct(getDietaryPrefs())

//    private fun isPreferenceSet(): Boolean {
//        getDietaryPrefs().also { p ->
//            if (p.isFair) return true
//            if (p.isFish) return true
//            if (p.isPoultry) return true
//            if (p.isLamb) return true
//            if (p.isVital) return true
//            if (p.isBeef) return true
//            if (p.isPork) return true
//            if (p.isVegan) return true
//            if (p.isVegetarian) return true
//            if (p.isGame) return true
//        }
//        return false
//    }

    fun setPreference(pref: DietaryPreferences, newValue: Boolean) {
//        prefs.edit().putBoolean(pref.value, newValue).apply()
        val oldString = getDietaryPrefs().toMutableList()
        when (pref) {
            DietaryPreferences.FAIR -> oldString[0] = newValue.toChar()
            DietaryPreferences.FISH -> oldString[1] = newValue.toChar()
            DietaryPreferences.POULTRY -> oldString[2] = newValue.toChar()
            DietaryPreferences.LAMB -> oldString[3] = newValue.toChar()
            DietaryPreferences.VITAL -> oldString[4] = newValue.toChar()
            DietaryPreferences.BEEF -> oldString[5] = newValue.toChar()
            DietaryPreferences.PORK -> oldString[6] = newValue.toChar()
            DietaryPreferences.VEGAN -> oldString[7] = newValue.toChar()
            DietaryPreferences.VEGETARIAN -> oldString[8] = newValue.toChar()
            DietaryPreferences.GAME -> oldString[9] = newValue.toChar()
        }
        val newString = oldString.joinToString("")
        Log.d("eek!4", newString)
        prefs.edit().putString(dietaryPrefString, newString).apply()
    }

    private fun Boolean.toChar() = if (this) 't' else 'f'

    fun getPreference(pref: DietaryPreferences): Boolean {
        val prefString = getDietaryPrefs().toList()
        return when (pref) {
            DietaryPreferences.FAIR -> prefString[0] == 't'
            DietaryPreferences.FISH -> prefString[1] == 't'
            DietaryPreferences.POULTRY -> prefString[2] == 't'
            DietaryPreferences.LAMB -> prefString[3] == 't'
            DietaryPreferences.VITAL -> prefString[4] == 't'
            DietaryPreferences.BEEF -> prefString[5] == 't'
            DietaryPreferences.PORK -> prefString[6] == 't'
            DietaryPreferences.VEGAN -> prefString[7] == 't'
            DietaryPreferences.VEGETARIAN -> prefString[8] == 't'
            DietaryPreferences.GAME -> prefString[9] == 't'
        }
    }
//        prefs.getBoolean(pref.value, false)

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