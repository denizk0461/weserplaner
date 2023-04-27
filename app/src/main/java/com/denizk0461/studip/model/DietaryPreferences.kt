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
    NONE("hasNone"),
    ERROR("onError")
    ;

    companion object {

        /**
         * Provides a converting function between
         */
        fun getData(index: Int): Pair<Int, Int> = when (index) {
            FAIR.ordinal -> Pair(R.string.pref_fair, R.drawable.handshake)
            FISH.ordinal -> Pair(R.string.pref_fish, R.drawable.fish)
            POULTRY.ordinal -> Pair(R.string.pref_poultry, R.drawable.chicken)
            LAMB.ordinal -> Pair(R.string.pref_lamb, R.drawable.sheep)
            VITAL.ordinal -> Pair(R.string.pref_vital, R.drawable.yoga)
            BEEF.ordinal -> Pair(R.string.pref_beef, R.drawable.cow)
            PORK.ordinal -> Pair(R.string.pref_pork, R.drawable.pig)
            VEGAN.ordinal -> Pair(R.string.pref_vegan, R.drawable.leaf)
            VEGETARIAN.ordinal -> Pair(R.string.pref_vegetarian, R.drawable.carrot)
            GAME.ordinal -> Pair(R.string.pref_game, R.drawable.deer)
            NONE.ordinal -> Pair(R.string.question_mark, R.drawable.circle)
            else -> Pair(R.string.question_mark, R.drawable.cross) // ERROR.ordinal
        }
    }
}