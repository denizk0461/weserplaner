package com.denizk0461.studip.model

import com.denizk0461.studip.R

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
    ;

    companion object {
        /**
         * Provides a converting function between the index of a dietary preference and its according
         * icon.
         * TODO this seems inefficient
         */
        val indexToDrawable: Map<Int, Int> = mapOf(
            0 to R.drawable.handshake,
            1 to R.drawable.fish,
            2 to R.drawable.chicken,
            3 to R.drawable.sheep,
            4 to R.drawable.yoga,
            5 to R.drawable.cow,
            6 to R.drawable.pig,
            7 to R.drawable.leaf,
            8 to R.drawable.carrot,
            9 to R.drawable.deer,
            10 to R.drawable.circle,
            11 to R.drawable.cross,
        )

        /**
         * Provides a converting function between the index of a dietary preference and its
         * according localised text string.
         * TODO this seems inefficient
         */
        val indexToString: Map<Int, Int> = mapOf(
            0 to R.string.pref_fair,
            1 to R.string.pref_fish,
            2 to R.string.pref_poultry,
            3 to R.string.pref_lamb,
            4 to R.string.pref_vital,
            5 to R.string.pref_beef,
            6 to R.string.pref_pork,
            7 to R.string.pref_vegan,
            8 to R.string.pref_vegetarian,
            9 to R.string.pref_game,
            10 to 0, // shouldn't occur
        )
    }
}