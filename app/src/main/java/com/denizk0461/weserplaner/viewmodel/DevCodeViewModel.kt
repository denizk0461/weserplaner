package com.denizk0461.weserplaner.viewmodel

import android.app.Application

class DevCodeViewModel(application: Application) : AppViewModel(application) {

    /**
     * Deletes everything from the database.
     */
    fun nukeEverything() {
        nukeEvents()
        nukeOfferItems()
        nukeOfferCategories()
        nukeOfferCanteens()
        nukeOfferDates()
    }

    /**
     * Deletes all Stud.IP events from the database.
     */
    fun nukeEvents() { doAsync { repo.nukeEvents() } }

    /**
     * Deletes all canteen items from the database.
     */
    fun nukeOfferItems() { doAsync { repo.nukeOfferItems() } }

    /**
     * Deletes all canteen categories from the database.
     */
    fun nukeOfferCategories() { doAsync { repo.nukeOfferCategories() } }

    /**
     * Deletes all canteens from the database.
     */
    fun nukeOfferCanteens() { doAsync { repo.nukeOfferCanteens() } }

    /**
     * Deletes all canteen dates from the database.
     */
    fun nukeOfferDates() { doAsync { repo.nukeOfferDates() } }
}