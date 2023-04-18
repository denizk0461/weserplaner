package com.denizk0461.studip.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
//    val isFair: Boolean,
//    val isFish: Boolean,
//    val isPoultry: Boolean,
//    val isLamb: Boolean,
//    val isVital: Boolean,
//    val isBeef: Boolean,
//    val isPork: Boolean,
//    val isVegan: Boolean,
//    val isVegetarian: Boolean,
//    val isGame: Boolean,
)