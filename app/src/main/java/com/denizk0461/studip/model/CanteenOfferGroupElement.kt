package com.denizk0461.studip.model

/**
 * Stripped version of CanteenOffer.kt that only carries essential info. Assumes that instances of
 * this are assigned to corresponding date, canteen, and category via an instance of
 * CanteenOfferGroup.kt.
 *
 * @param title         text content of the individual canteen offer
 * @param price         students' price for the individual canteen offer
 * @param dietaryPreferences    dietary preferences used to filter for the user's needs - see
 *                              DietaryPrefObject.kt
 * @param allergens             allergens and additives that are present in the item
 */
data class CanteenOfferGroupElement(
    val title: String,
    val price: String,
    val dietaryPreferences: String,
    val allergens: String,
)
