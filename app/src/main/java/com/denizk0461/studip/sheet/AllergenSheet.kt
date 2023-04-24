package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.denizk0461.studip.R
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.ItemSheetPreferenceBinding
import com.denizk0461.studip.databinding.SheetAllergenBinding
import com.denizk0461.studip.model.Allergens
import com.denizk0461.studip.model.CanteenOfferGroupElement
import com.denizk0461.studip.model.DietaryPreferences

/**
 * This class is used to display further information on a canteen offer to the user. Unlike the name
 * implies, it displays more than just allergens.
 */
class AllergenSheet(
    private val offer: CanteenOfferGroupElement,
    private val category: String,
) : AppSheet(R.layout.sheet_allergen) {

    private val binding: SheetAllergenBinding by viewBinding(SheetAllergenBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            // Category header
            textCategory.text = getString(R.string.sheet_category, category)

            // Offer text
            textTitle.text = offer.title

            // Iterate through all dietary preferences
            offer.dietaryPreferences.toList().forEachIndexed { index, c ->
                // If a preference is met, inflate a view to display it
                if (c == 't') {
                    // Inflate the view
                    val prefLine = ItemSheetPreferenceBinding.inflate(LayoutInflater.from(context))

                    // Add the localised text for the preference
                    prefLine.preferenceText.text = getString(DietaryPreferences.indexToString[index]!!)

                    // Set the appropriate icon
                    prefLine.preferenceImage.setImageDrawable(
                        getDrawable(
                            context,
                            DietaryPreferences.indexToDrawable[index]!!,
                        )
                    )

                    // Add the view to the sheet's view container
                    containerPreferences.addView(prefLine.root)
                }
            }

            // Allergen and additive notice
            textContent.text = offer.allergens.compileAllergenString()

            // Show a price, if one is available
            if (offer.price.isBlank()) {
                textPrice.visibility = View.GONE
            } else {
                textPrice.visibility = View.VISIBLE
                textPrice.text = offer.price
            }
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