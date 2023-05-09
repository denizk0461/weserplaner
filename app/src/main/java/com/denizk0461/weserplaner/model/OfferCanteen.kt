package com.denizk0461.weserplaner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for binding several canteen items to a specific date. Registered in the app database.
 *
 * @param id            primary key that uniquely identifies the item
 * @param canteen       name of the canteen
 * @param openingHours  opening hours of the canteen
 * @param news          news about the canteen
 */
@Entity(
    tableName = "offer_canteen",
)
data class OfferCanteen(
    @PrimaryKey val id: Int,
    val canteen: String,
    val openingHours: String,
    val news: String,
)