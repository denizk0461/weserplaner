package com.denizk0461.studip.model

/**
 * Grouping element that holds instances of CanteenOfferGroupElement.kt that share date, category,
 * and canteen values. Used to filter more easily
 *
 * @param date      date formatted as dd.MM.
 * @param dateId    ID of the corresponding OfferDate.kt instance
 * @param category  category text content of the item
 * @param canteen   name of the canteen
 * @param offers    elements of CanteenOfferGroupElement.kt with common values
 */
data class CanteenOfferGroup(
    val date: String,
    val dateId: Int,
    val category: String,
    val canteen: String,
    val offers: List<CanteenOfferGroupElement>,
)
