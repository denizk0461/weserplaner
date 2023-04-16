package com.denizk0461.studip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offers")
data class CanteenOffer(
    @PrimaryKey val id: Int,
    val date: String,
    val dateId: Int, // gives every item on the same day the same ID for easier filtering
//    val canteen: Int,
    val category: String,
    val title: String,
    val price: String, // price for students
    val isFair: Boolean, // artgerechte Tierhaltung
    val isFish: Boolean, // Fisch
    val isPoultry: Boolean, // Geflügel
    val isLamb: Boolean, // Lamm
    val isVital: Boolean, // mensaVital
    val isBeef: Boolean, // Rindfleisch
    val isPork: Boolean, // Schweinefleisch
    val isVegan: Boolean, // plant-based (vegan)
    val isVegetarian: Boolean, // vegetarisch
    val isGame: Boolean, // Wild
)
