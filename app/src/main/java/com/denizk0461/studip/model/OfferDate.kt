package com.denizk0461.studip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "offer_date",
)
data class OfferDate(
    @PrimaryKey val id: Int,
    val date: String,
)
