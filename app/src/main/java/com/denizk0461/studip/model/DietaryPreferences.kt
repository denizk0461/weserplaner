package com.denizk0461.studip.model

/**
 * Enumeration that can be used in conjunction with DietaryPrefObject.kt to more concisely look up,
 * specify, and iterate through dietary preferences. Order of these elements matches the order used
 * on the website of the Studierendenwerk Bremen.
 *
 * @param value string value that can be used where the enum value itself cannot be used (e.g.
 *              SharedPreferences)
 */
enum class DietaryPreferences(val value: String) {
    FAIR("isFair"),
    FISH("isFish"),
    POULTRY("isPoultry"),
    LAMB("isLamb"),
    VITAL("isVital"),
    BEEF("isBeef"),
    PORK("isPork"),
    VEGAN("isVegan"),
    VEGETARIAN("isVegetarian"),
    GAME("isGame"),
}