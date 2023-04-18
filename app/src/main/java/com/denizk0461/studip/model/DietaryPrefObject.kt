package com.denizk0461.studip.model

data class DietaryPrefObject(
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