package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import com.denizk0461.studip.R
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetAllergenBinding
import com.denizk0461.studip.model.Allergens
import com.denizk0461.studip.model.CanteenOfferGroupElement

class AllergenSheet(private val offer: CanteenOfferGroupElement) : AppSheet(R.layout.sheet_allergen) {

    private val binding: SheetAllergenBinding by viewBinding(SheetAllergenBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            title.text = offer.title
            textContent.text = offer.allergens.compileAllergenString()
        }
    }

    /**
     * Retrieves localised strings for all allergens present in a given offer and compiles them into
     * a human-readable string, if any allergens are present.
     *
     * @return  a string of all allergens
     */
    private fun String.compileAllergenString(): String {

        // Let the user know if no allergens are present
        if (this.isBlank()) return getString(R.string.allergens_none)

        // Stores all localised allergens
        var allergenString = ""

        // Used to insert a delimiter if more than one allergen is found
        var isFirst = true

        // Split all allergens to iterate through them
        val allergens = this.replace(" ", "").split(",")

        // Iterate through all allergens
        allergens.forEach { allergen ->
            // Insert a delimiter if more than one allergen is found
            if (!isFirst) allergenString += ", "

            // Add a localised string for the allergen
            allergenString += getString(Allergens.getStringRes(allergen))

            // Set when the first allergen has been processed
            isFirst = false
        }

        // Insert the allergens into a template string
        return getString(R.string.allergens_contains, allergenString)
    }
}