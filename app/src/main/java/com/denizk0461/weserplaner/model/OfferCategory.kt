package com.denizk0461.weserplaner.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entity for storing categories/headers grouping individual instances of OfferItem.kt. Registered
 * in the app database.
 *
 * @param id        key that uniquely identifies the item
 * @param dateId    foreign key binding the item to an instance of OfferDate.kt
 * @param canteenId foreign key binding the item to an instance of OfferCanteen.kt
 * @param category  text content of the item
 */
@Entity(
    tableName = "offer_category",
    foreignKeys = [
        ForeignKey(
            OfferDate::class,
            ["id"],
            ["dateId"],
            ForeignKey.CASCADE,
        ),
        ForeignKey(
            OfferCanteen::class,
            ["id"],
            ["canteenId"],
            ForeignKey.CASCADE,
        ),
    ]
)
data class OfferCategory(
    @PrimaryKey val id: Int,
    val dateId: Int,
    val canteenId: Int,
    val category: String,
)
