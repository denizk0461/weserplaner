package com.denizk0461.studip.model

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
    @PrimaryKey val id: Int,
    val categoryId: Int,
    val title: String,
    val price: String,
    val isFair: Boolean,
    val isFish: Boolean,
    val isPoultry: Boolean,
    val isLamb: Boolean,
    val isVital: Boolean,
    val isBeef: Boolean,
    val isPork: Boolean,
    val isVegan: Boolean,
    val isVegetarian: Boolean,
    val isGame: Boolean,
)