package com.denizk0461.weserplaner.sheet

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.getParcelableCompat
import com.denizk0461.weserplaner.data.getThemedColor
import com.denizk0461.weserplaner.data.showToast
import com.denizk0461.weserplaner.data.viewBinding
import com.denizk0461.weserplaner.databinding.ItemSheetPreferenceBinding
import com.denizk0461.weserplaner.databinding.SheetAllergenBinding
import com.denizk0461.weserplaner.exception.ParcelNotFoundException
import com.denizk0461.weserplaner.model.Allergens
import com.denizk0461.weserplaner.model.CanteenOfferGroupElement
import com.denizk0461.weserplaner.model.DietaryPreferences

/**
 * This class is used to display further information on a canteen offer to the user. Unlike the name
 * implies, it displays more than just allergens.
 */
class AllergenSheet : AppSheet(R.layout.sheet_allergen) {

    // View binding
    private val binding: SheetAllergenBinding by viewBinding(SheetAllergenBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            // Retrieve offer as Parcelable from arguments bundle
            val offer: CanteenOfferGroupElement = arguments.getParcelableCompat("offer")

            // Retrieve category string
            val category = arguments?.getString("category") ?: ""

            // Retrieve preference on whether the user wants preferences to be coloured
            val displayColours = arguments?.getBoolean("preferenceColour") == true

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
                        val prefLine =
                            ItemSheetPreferenceBinding.inflate(LayoutInflater.from(context))

                        val (stringId, drawableId, colourId) = DietaryPreferences.getData(index)

                        // Add the localised text for the preference
                        prefLine.preferenceText.text = getString(stringId)

                        // Set the appropriate icon
                        prefLine.preferenceImage.setImageDrawable(
                            getDrawable(
                                context,
                                drawableId,
                            )
                        )

                        // Set tint of the icon; themed to the preference, if the user has the option enabled
                        prefLine.preferenceImage.imageTintList = ColorStateList.valueOf(
                            context.theme.getThemedColor(
                                if (displayColours) {
                                    colourId
                                } else {
                                    R.attr.colorText
                                }
                            )
                        )

                        // Add the view to the sheet's view container
                        containerPreferences.addView(prefLine.root)
                    }
                }

                // Retrieve allergen and additive strings
                val (allergens, additives) = offer.allergens.compileStrings()

                // Display notices for allergens and additives, if necessary
                if (allergens.isBlank() && additives.isBlank()) {
                    // Tell the user that no allergens and no additives are present in the offer
                    textAllergensTitle.text = getString(R.string.allergens_none)
                    textAllergensContent.visibility = View.GONE
                    textAdditivesTitle.visibility = View.GONE
                    textAdditivesContent.visibility = View.GONE
                } else {
                    // Set allergen and additives accordingly if any are present
                    textAllergensTitle.text = getString(R.string.allergens_allergens_title)
                    textAllergensContent.text = getString(
                        R.string.allergens_content_template,
                        allergens,
                    )
                    textAdditivesTitle.text = getString(R.string.allergens_additives_title)
                    textAdditivesContent.text = getString(
                        R.string.allergens_content_template,
                        additives,
                    )
                }

                // Show a price, if one is available
                if (offer.price.isBlank()) {
                    textPrice.visibility = View.GONE
                } else {
                    textPrice.visibility = View.VISIBLE
                    textPrice.text = offer.price
                }
            }

            // Set up close button
            binding.buttonCancel.setOnClickListener {
                // Do nothing and dismiss the sheet
                dismiss()
            }
        } catch (e: ParcelNotFoundException) {
            // Tell the user that something went wrong when retrieving Parcelable
            showToast(context, getString(R.string.canteen_page_allergen_sheet_error))

            // Close the sheet
            dismiss()
        }
    }

    /**
     * Retrieves localised strings for all allergens and additives present in a given offer and
     * compiles them into human-readable strings, if any are present.
     *
     * @receiver    string to parse allergens and additives from
     * @return      allergens and additives formatted as strings
     */
    private fun String.compileStrings(): Pair<String, String> {

        // Return empty values if the received string is blank
        if (this.isBlank()) return Pair("", "")

        // Stores all localised allergens
        val allergenList = mutableListOf<String>()

        // Stores all localised additives
        val additiveList = mutableListOf<String>()

        // Split all allergens to iterate through them
        val allergens = this.replace(" ", "").split(",")

        // Iterate through all items
        allergens.forEach { allergen ->

            // Check if the item is an allergen or an additive
            if (allergen.toIntOrNull() == null) {
                // If the item is not numeric, it is an allergen. Add this to the allergens.
                allergenList.add(getString(Allergens.getStringRes(allergen)))
            } else {
                // If the item is numeric, it is an additive. Add this to the additives.
                additiveList.add(getString(Allergens.getStringRes(allergen)))
            }
        }

        /*
         * Join the lists to formatted strings, separated by commas. Put a localised "none" if
         * either no allergens or additives could be found respectively.
         */
        return Pair(
            allergenList.joinToString(", ").ifBlank {
                getString(R.string.allergens_single_none)
            },
            additiveList.joinToString(", ").ifBlank {
                getString(R.string.allergens_single_none)
            },
        )
    }
}