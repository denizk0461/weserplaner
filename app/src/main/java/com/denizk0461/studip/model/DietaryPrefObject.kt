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
) {
    companion object {
        private const val C_TRUE = 't'
        private const val C_FALSE = '.'
        /* assume that the values must be parsed as follows:
         * "xxxxxxxxxx"
         * t = true, . = false
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