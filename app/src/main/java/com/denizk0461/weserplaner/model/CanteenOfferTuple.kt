package com.denizk0461.weserplaner.model

/**
 * Serves to retrieve information about a specified canteen from the database using a single query.
 *
 * @param openingHours  opening hours of the canteen
 * @param news          news that may be available for the canteen
 */
data class CanteenOfferTuple(
    val openingHours: String,
    val news: String,
)