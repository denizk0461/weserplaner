package com.denizk0461.studip.model

// TODO KDoc
enum class AllergenPreferences {
    GLUTEN_WHEAT,
    GLUTEN_RYE,
    GLUTEN_BARLEY,
    GLUTEN_OATS,
    GLUTEN_SPELT,
    GLUTEN_KAMUT,
    CRUSTACEANS,
    EGGS,
    FISH,
    PEANUTS,
    SOY,
    DAIRY,
    ALMONDS,
    HAZELNUTS,
    WALNUTS,
    CASHEW_NUTS,
    PECANS,
    BRAZIL_NUTS,
    PISTACHIOS,
    MACADAMIA,
    CELERY,
    MUSTARD,
    SULPHIDES,
    LUPINS,
    SESAME,
    MOLLUSCS,
    ;

    /**
     * Object that holds allergy preferences. Can be used to hold both the user-set values as well
     * as the values of an individual canteen item.
     *
     * @param hasWheat
     * @param hasRye
     * @param hasBarley
     * @param hasOats
     * @param hasSpelt
     * @param hasKamut
     * @param hasCrustaceans
     * @param hasEggs
     * @param hasFish
     * @param hasPeanuts
     * @param hasSoy
     * @param hasDairy
     * @param hasAlmonds
     * @param hasHazelnuts
     * @param hasWalnuts
     * @param hasCashewNuts
     * @param hasPecans
     * @param hasBrazilNuts
     * @param hasPistachios
     * @param hasMacadamia
     * @param hasCelery
     * @param hasMustard
     * @param hasSulphides
     * @param hasLupins
     * @param hasSesame
     * @param hasMolluscs
     */
    data class Object(
        val hasWheat: Boolean,
        val hasRye: Boolean,
        val hasBarley: Boolean,
        val hasOats: Boolean,
        val hasSpelt: Boolean,
        val hasKamut: Boolean,
        val hasCrustaceans: Boolean,
        val hasEggs: Boolean,
        val hasFish: Boolean,
        val hasPeanuts: Boolean,
        val hasSoy: Boolean,
        val hasDairy: Boolean,
        val hasAlmonds: Boolean,
        val hasHazelnuts: Boolean,
        val hasWalnuts: Boolean,
        val hasCashewNuts: Boolean,
        val hasPecans: Boolean,
        val hasBrazilNuts: Boolean,
        val hasPistachios: Boolean,
        val hasMacadamia: Boolean,
        val hasCelery: Boolean,
        val hasMustard: Boolean,
        val hasSulphides: Boolean,
        val hasLupins: Boolean,
        val hasSesame: Boolean,
        val hasMolluscs: Boolean,
    ) {

        /**
         * Deconstructs an AllergenPreferencesObject to a string.
         *
         * @return      string with preferences inserted
         */
        fun deconstruct(): String {
            val array = arrayListOf<String>()

            if (hasWheat) array.add("a1")
            if (hasRye) array.add("a2")
            if (hasBarley) array.add("a3")
            if (hasOats) array.add("a4")
            if (hasSpelt) array.add("a5")
            if (hasKamut) array.add("a6")
            if (hasCrustaceans) array.add("b")
            if (hasEggs) array.add("c")
            if (hasFish) array.add("d")
            if (hasPeanuts) array.add("e")
            if (hasSoy) array.add("f")
            if (hasDairy) array.add("g")
            if (hasAlmonds) array.add("h1")
            if (hasHazelnuts) array.add("h2")
            if (hasWalnuts) array.add("h3")
            if (hasCashewNuts) array.add("h4")
            if (hasPecans) array.add("h5")
            if (hasBrazilNuts) array.add("h6")
            if (hasPistachios) array.add("h7")
            if (hasMacadamia) array.add("h8")
            if (hasCelery) array.add("i")
            if (hasMustard) array.add("j")
            if (hasSulphides) array.add("k")
            if (hasLupins) array.add("l")
            if (hasSesame) array.add("m")
            if (hasMolluscs) array.add("n")

            return array.joinToString(",")
        }
    }

    companion object {
        const val C_TRUE: Char = 'y'
        const val C_FALSE: Char = 'n'

        const val TEMPLATE: String = ""

        /**
         * Constructs an AllergenPreferenceObject from a string.
         *
         * @param input string that must be 26 characters long
         * @return      instance of AllergenPreferences.Object with preferences inserted
         */
        fun construct(input: String): Object {
            val elements = input.split(",")
            return Object(
                elements.contains("a1"),
                elements.contains("a2"),
                elements.contains("a3"),
                elements.contains("a4"),
                elements.contains("a5"),
                elements.contains("a6"),
                elements.contains("b"),
                elements.contains("c"),
                elements.contains("d"),
                elements.contains("e"),
                elements.contains("f"),
                elements.contains("g"),
                elements.contains("h1"),
                elements.contains("h2"),
                elements.contains("h3"),
                elements.contains("h4"),
                elements.contains("h5"),
                elements.contains("h6"),
                elements.contains("h7"),
                elements.contains("h8"),
                elements.contains("i"),
                elements.contains("j"),
                elements.contains("k"),
                elements.contains("l"),
                elements.contains("m"),
                elements.contains("n"),
            )
        }
//        String(
//            charArrayOf(
//                charFor(obj.hasWheat),
//                charFor(obj.hasRye),
//                charFor(obj.hasBarley),
//                charFor(obj.hasOats),
//                charFor(obj.hasSpelt),
//                charFor(obj.hasKamut),
//                charFor(obj.hasCrustaceans),
//                charFor(obj.hasEggs),
//                charFor(obj.hasFish),
//                charFor(obj.hasPeanuts),
//                charFor(obj.hasSoy),
//                charFor(obj.hasDairy),
//                charFor(obj.hasAlmonds),
//                charFor(obj.hasHazelnuts),
//                charFor(obj.hasWalnuts),
//                charFor(obj.hasCashewNuts),
//                charFor(obj.hasPecans),
//                charFor(obj.hasBrazilNuts),
//                charFor(obj.hasPistachios),
//                charFor(obj.hasMacadamia),
//                charFor(obj.hasCelery),
//                charFor(obj.hasMustard),
//                charFor(obj.hasSulphides),
//                charFor(obj.hasLupins),
//                charFor(obj.hasSesame),
//                charFor(obj.hasMolluscs),
//            )
//        )

        private fun charFor(pref: Boolean) = if (pref) C_TRUE else C_FALSE
    }
}