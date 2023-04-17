package com.denizk0461.studip.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "offer_category",
    foreignKeys = [
        ForeignKey(
            OfferDate::class,
            ["id"],
            ["date_id"],
            ForeignKey.CASCADE,
        ),
        ForeignKey(
            OfferCanteen::class,
            ["id"],
            ["canteen_id"],
            ForeignKey.CASCADE,
        ),
    ]
)
data class OfferCategory(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "date_id") val dateId: Int,
    @ColumnInfo(name = "canteen_id") val canteenId: Int,
    val category: String,
)
