package com.denizk0461.weserplaner.model

/**
 * Class for managing allergen preferences. The user can tell the app which substances they are
 * allergic against, and the functions in this class can be used to filter out offers containing
 * allergens that could kill them.
 */
class AllergenPreferences {

    /**
     * Object that holds allergy preferences. Can be used to hold both the user-set values as well
     * as the values of an individual canteen item.
     *
     * @param hasWheat          whether the offer contains gluten through wheat
     * @param hasRye            whether the offer contains gluten through rye
     * @param hasBarley         whether the offer contains gluten through barley
     * @param hasOats           whether the offer contains gluten through oats
     * @param hasSpelt          whether the offer contains gluten through spelt
     * @param hasKamut          whether the offer contains gluten through kamut
     * @param hasCrustaceans    whether the offer contains crustaceans
     * @param hasEggs           whether the offer contains eggs
     * @param hasFish           whether the offer contains fish
     * @param hasPeanuts        whether the offer contains peanuts
     * @param hasSoy            whether the offer contains soy
     * @param hasDairy          whether the offer contains dairy
     * @param hasAlmonds        whether the offer contains almonds
     * @param hasHazelnuts      whether the offer contains hazelnuts
     * @param hasWalnuts        whether the offer contains walnuts
     * @param hasCashewNuts     whether the offer contains cashew nuts
     * @param hasPecans         whether the offer contains pecan nuts
     * @param hasBrazilNuts     whether the offer contains brazil nuts
     * @param hasPistachios     whether the offer contains pistachios
     * @param hasMacadamia      whether the offer contains macadamia nuts
     * @param hasCelery         whether the offer contains celery
     * @param hasMustard        whether the offer contains mustard
     * @param hasSulphides      whether the offer contains highly concentrated sulphides
     * @param hasLupins         whether the offer contains lupins
     * @param hasSesame         whether the offer contains sesame
     * @param hasMolluscs       whether the offer contains molluscs
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
            // Create an empty list
            val array = arrayListOf<String>()

            /*
             * Add the symbols for the allergens to the list. Allergen symbols derived from the
             * allergen list at stw-bremen.de
             */
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

            // Join the individual symbols to a single string
            return array.joinToString(",")
        }
    }

    companion object {

        /**
         * Template string representing no allergens.
         */
        const val TEMPLATE: String = ""

        /**
         * Constructs an AllergenPreferenceObject from a string.
         *
         * @param input string that must be 26 characters long. This will NOT be checked
         * @return      instance of AllergenPreferences.Object with preferences inserted
         */
        fun construct(input: String): Object {
            // Split the string into its individual components
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
    }
}