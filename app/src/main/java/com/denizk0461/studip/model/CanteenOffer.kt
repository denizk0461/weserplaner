package com.denizk0461.studip.model

//@Entity(tableName = "offers")
data class CanteenOffer(
//    @PrimaryKey val id: Int,
    val id: Int,
    val date: String,
    val dateId: Int,
    val category: String,
    val categoryId: Int,
    val canteen: String,
    val canteenId: Int,
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
