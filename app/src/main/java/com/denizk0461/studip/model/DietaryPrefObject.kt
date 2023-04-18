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
        /* assume that the values must be parsed as follows:
         * "xxxxxxxxxx"
         * t = true, f = false
         */
        fun construct(values: String) = DietaryPrefObject(
            isFair = values[0] == 't',
            isFish = values[1] == 't',
            isPoultry = values[2] == 't',
            isLamb = values[3] == 't',
            isVital = values[4] == 't',
            isBeef = values[5] == 't',
            isPork = values[6] == 't',
            isVegan = values[7] == 't',
            isVegetarian = values[8] == 't',
            isGame = values[9] == 't',
        )
    }

    fun deconstruct() = String(
        charArrayOf(
            if (this.isFair) 't' else 'f',
            if (this.isFish) 't' else 'f',
            if (this.isPoultry) 't' else 'f',
            if (this.isLamb) 't' else 'f',
            if (this.isVital) 't' else 'f',
            if (this.isBeef) 't' else 'f',
            if (this.isPork) 't' else 'f',
            if (this.isVegan) 't' else 'f',
            if (this.isVegetarian) 't' else 'f',
            if (this.isGame) 't' else 'f',
        )
    )
}