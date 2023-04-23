package com.denizk0461.studip.model

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
) {
    companion object {
        // Char used to construct a regex to denote that a preference is met
        const val C_TRUE = 't'
        // Char used to construct a regex to denote that a preference is not met
        const val C_FALSE = '.'

        /**
         * Constructs a DietaryPrefObject from a regex string. String must be 10 characters long.
         * A char equal to C_TRUE is treated as a true boolean value. Any other char is treated as a
         * false boolean value.
         *
         * @param values the regex string
         * @return an instance of DietaryPrefObject
         */
        fun construct(values: String) = DietaryPrefObject(
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
    }

    /**
     * Constructs a regular expression string from a DietaryPrefObject. This can be used to filter for specific
     * preferences. Example:
     * .......t..
     * This expression filters for items that are vegan ('t' in position 8) and ignores all other
     * preference options ('.' in all other positions).
     *
     * @return a 10-character long regular expression
     */
    fun deconstruct() = String(
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