package com.denizk0461.studip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "offer_canteen",
)
data class OfferCanteen(
    @PrimaryKey val id: Int,
    val canteen: String,
)