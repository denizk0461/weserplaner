package com.denizk0461.studip.model

import androidx.room.ColumnInfo

//@Entity(tableName = "offers")
data class CanteenOffer(
//    @PrimaryKey val id: Int,
    val itemId: Int,
    val date: String,
    val dateId: Int,
    val category: String,
    val categoryId: Int,
    val canteen: String,
    val canteenId: Int,
    val title: String,
    val price: String, // price for students
    @ColumnInfo(name = "dietary_preferences") val dietaryPreferences: String,
)
