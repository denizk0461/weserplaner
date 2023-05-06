package com.denizk0461.weserplaner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Entity for storing individual items of a university canteen. Registered in the app database.
 *
 * @param itemId                primary key that uniquely identifies the item
 * @param categoryId            foreign key binding the item to an instance of OfferCategory.kt
 * @param title                 text content of the individual canteen offer
 * @param price                 students' price for the individual canteen offer
 * @param dietaryPreferences    dietary preferences used to filter for the user's needs - see
 *                              [DietaryPreferences]
 * @param allergens             allergens and additives that are present in the item
 */
@Entity(
    tableName = "offer_item",
    foreignKeys = [
        ForeignKey(
            OfferCategory::class,
            ["id"],
            ["categoryId"],
            ForeignKey.CASCADE,
        ),
    ]
)
data class OfferItem(
    @PrimaryKey val itemId: Int,
    val categoryId: Int,
    val title: String,
    val price: String,
    @ColumnInfo(name = "dietary_preferences") val dietaryPreferences: String,
    val allergens: String,
)