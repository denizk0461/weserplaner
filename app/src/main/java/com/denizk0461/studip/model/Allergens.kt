package com.denizk0461.studip.model

import com.denizk0461.studip.R

object Allergens {
    /**
     * Converts an allergen into the string ID for its localised full-text version. Elements are
     * derived from the website of the Studierendenwerk Bremen:
     * https://www.stw-bremen.de/de/mensa/allergene
     *
     * @param value allergen that will be converted
     * @return      string resource ID for the allergen
     */
    fun getStringRes(value: String): Int = when (value) {
        // Additives
        "1" -> R.string.allergen_dye
        "2" -> R.string.allergen_preservatives
        "3" -> R.string.allergen_antioxidants
        "4" -> R.string.allergen_phosphates
        "5" -> R.string.allergen_sweeteners
        "6" -> R.string.allergen_phenylalanine
        "7" -> R.string.allergen_sulphured
        "8" -> R.string.allergen_blackened
        "9" -> R.string.allergen_waxed
        "10" -> R.string.allergen_alcohol
        "11" -> R.string.allergen_flavour_enhancers
        "12" -> R.string.allergen_caffeine
        "13" -> R.string.allergen_rennet
        // Allergens
        "a1" -> R.string.allergen_wheat
        "a2" -> R.string.allergen_rye
        "a3" -> R.string.allergen_barley
        "a4" -> R.string.allergen_oat
        "a5" -> R.string.allergen_spelt
        "a6" -> R.string.allergen_kamut
        "b" -> R.string.allergen_crustaceans
        "c" -> R.string.allergen_eggs
        "d" -> R.string.allergen_fish
        "e" -> R.string.allergen_peanuts
        "f" -> R.string.allergen_soy
        "g" -> R.string.allergen_dairy
        "h1" -> R.string.allergen_almonds
        "h2" -> R.string.allergen_hazelnuts
        "h3" -> R.string.allergen_walnuts
        "h4" -> R.string.allergen_cashew_nuts
        "h5" -> R.string.allergen_pecans
        "h6" -> R.string.allergen_brazil_nuts
        "h7" -> R.string.allergen_pistachios
        "h8" -> R.string.allergen_macadamia
        "i" -> R.string.allergen_celery
        "j" -> R.string.allergen_mustard
        "k" -> R.string.allergen_sulphides
        "l" -> R.string.allergen_lupins
        "m" -> R.string.allergen_sesame
        "n" -> R.string.allergen_molluscs
        else -> 0 // shouldn't occur
    }
}