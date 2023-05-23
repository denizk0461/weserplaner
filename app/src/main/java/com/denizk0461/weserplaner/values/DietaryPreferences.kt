package com.denizk0461.weserplaner.values

import com.denizk0461.weserplaner.R

/**
 * Enumeration for more concisely looking up, specifying, and iterating through dietary preferences.
 * Order of these elements matches the order used on the website of the Studierendenwerk Bremen.
 *
 * @param value string value that can be used where the enum value itself cannot be used (e.g.
 *              SharedPreferences)
 */
enum class DietaryPreferences(val value: String) {
    /**
     * Offer contains meat for which animal welfare standards were upheld.
     */
    WELFARE("isFair"),

    /**
     * Offer contains fish products.
     */
    FISH("isFish"),

    /**
     * Offer contains poultry (chicken) products.
     */
    POULTRY("isPoultry"),

    /**
     * Offer contains lamb products.
     */
    LAMB("isLamb"),

    /**
     * Offer contains products that are good for ur health i guess?
     */
    VITAL("isVital"),

    /**
     * Offer contains beef (cow) products.
     */
    BEEF("isBeef"),

    /**
     * Offer contains pork (pig) products.
     */
    PORK("isPork"),

    /**
     * Offer contains no animal products and is fully plant-based (vegan).
     */
    VEGAN("isVegan"),

    /**
     * Offer contains no animal parts and is vegetarian.
     */
    VEGETARIAN("isVegetarian"),

    /**
     * Offer contains game (wild animals such as deer, gosh, is that not barbaric? Sorry for being
     * dramatic, but I didn't even know that was served at our canteens until I started making this
     * app).
     */
    GAME("isGame"),

    /**
     * Offer doesn't fulfil any of the dietary preferences listed above.
     */
    NONE("hasNone"),

    /**
     * Offer has encountered an error.
     */
    ERROR("onError"),
    ;

    /**
     * Object that holds dietary preferences. Can be used to hold both the user-set values as well as
     * the values of an individual canteen item.
     *
     * @param isFair        item contains meat from fairly-treated animals (lol sure)
     * @param isFish        item contains fish
     * @param isPoultry     item contains chicken
     * @param isLamb        item contains lamb
     * @param isVital       i don't actually know
     * @param isBeef        item contains beef
     * @param isPork        item contains pork
     * @param isVegan       item is plant-based; contains no animal-derived ingredients
     * @param isVegetarian  item is vegetarian; contains no animal parts
     * @param isGame        item contains game meat (e.g. deer)
     */
    data class Object(
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
    ) {
        /**
         * Constructs a regular expression string from a DietaryPrefObject. This can be used to filter for specific
         * preferences. Example:
         * .......t..
         * This expression filters for items that are vegan ('t' in position 8) and ignores all other
         * preference options ('.' in all other positions).
         *
         * @return a 10-character long regular expression
         */
        fun deconstruct(): String = String(
            charArrayOf(
                if (this.isFair) C_TRUE else C_FALSE,
                if (this.isFish) C_TRUE else C_FALSE,
                if (this.isPoultry) C_TRUE else C_FALSE,
                if (this.isLamb) C_TRUE else C_FALSE,
                if (this.isVital) C_TRUE else C_FALSE,
                if (this.isBeef) C_TRUE else C_FALSE,
                if (this.isPork) C_TRUE else C_FALSE,
                if (this.isVegan) C_TRUE else C_FALSE,
                if (this.isVegetarian) C_TRUE else C_FALSE,
                if (this.isGame) C_TRUE else C_FALSE,
            )
        )
    }

    companion object {

        /**
         * Char used to construct a regex to denote that a preference is met.
         */
        const val C_TRUE: Char = 't'

        /**
         * Char used to construct a regex to denote that a preference is not met.
         */
        const val C_FALSE: Char = '.'

        /**
         * Default value for preferences if none are set.
         */
        const val TEMPLATE_EMPTY: String = ".........."

        /**
         * Object denoting that no dietary preferences apply.
         */
        val NONE_MET: Object = construct(TEMPLATE_EMPTY)

        /**
         * Constructs a DietaryPrefObject from a regex string. String must be 10 characters long.
         * A char equal to C_TRUE is treated as a true boolean value. Any other char is treated as a
         * false boolean value.
         *
         * @param values the regex string
         * @return an instance of DietaryPrefObject
         */
        fun construct(values: String): Object = Object(
            isFair = values[0] == C_TRUE,
            isFish = values[1] == C_TRUE,
            isPoultry = values[2] == C_TRUE,
            isLamb = values[3] == C_TRUE,
            isVital = values[4] == C_TRUE,
            isBeef = values[5] == C_TRUE,
            isPork = values[6] == C_TRUE,
            isVegan = values[7] == C_TRUE,
            isVegetarian = values[8] == C_TRUE,
            isGame = values[9] == C_TRUE,
        )

        /**
         * Provides a converting function between the ordinals of the enumeration and corresponding
         * string and drawable values.
         *
         * @param index ordinal of the enum item
         * @return      string resource ID, drawable resource ID, colour attribute ID
         */
        fun getData(index: Int): Triple<Int, Int, Int> = when (index) {
            WELFARE.ordinal -> Triple(R.string.pref_fair, R.drawable.handshake, R.attr.colorPrimaryFair)
            FISH.ordinal -> Triple(R.string.pref_fish, R.drawable.fish, R.attr.colorPrimaryFish)
            POULTRY.ordinal -> Triple(R.string.pref_poultry, R.drawable.chicken, R.attr.colorPrimaryPoultry)
            LAMB.ordinal -> Triple(R.string.pref_lamb, R.drawable.sheep, R.attr.colorPrimaryLamb)
            VITAL.ordinal -> Triple(R.string.pref_vital, R.drawable.yoga, R.attr.colorPrimaryVital)
            BEEF.ordinal -> Triple(R.string.pref_beef, R.drawable.cow, R.attr.colorPrimaryBeef)
            PORK.ordinal -> Triple(R.string.pref_pork, R.drawable.pig, R.attr.colorPrimaryPork)
            VEGAN.ordinal -> Triple(R.string.pref_vegan, R.drawable.leaf, R.attr.colorPrimaryVegan)
            VEGETARIAN.ordinal -> Triple(R.string.pref_vegetarian, R.drawable.carrot, R.attr.colorPrimaryVegetarian)
            GAME.ordinal -> Triple(R.string.pref_game, R.drawable.deer, R.attr.colorPrimaryGame)
            NONE.ordinal -> Triple(R.string.question_mark, R.drawable.circle, R.attr.colorText)
            else -> Triple(R.string.question_mark, R.drawable.cross, R.attr.colorErrorText) // ERROR.ordinal
        }
    }
}