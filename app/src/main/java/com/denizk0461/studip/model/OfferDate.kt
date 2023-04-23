package com.denizk0461.studip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for binding several canteen items to a specific date. Registered in the app database.
 *
 * @param id    primary key that uniquely identifies the item
 * @param date  date formatted as dd.MM.
 */
@Entity(
    tableName = "offer_date",
)
data class OfferDate(
    @PrimaryKey val id: Int,
    val date: String,
)
