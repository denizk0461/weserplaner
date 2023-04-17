package com.denizk0461.studip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
