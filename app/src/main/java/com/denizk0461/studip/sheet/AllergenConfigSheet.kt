package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import com.denizk0461.studip.R
import com.denizk0461.studip.data.showToast
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetAllergenConfigBinding
import com.denizk0461.studip.model.AllergenPreferences

/**
 * Configuration sheet for the user to pick substances they are allergic against. Offers containing
 * these allergens will be hidden.
 *
 * @param currentAllergenConfig currently set allergen preference
 * @param onUpdate              action to execute to store the allergen preferences
 */
class AllergenConfigSheet(
    private val currentAllergenConfig: AllergenPreferences.Object,
    private val onUpdate: (obj: AllergenPreferences.Object) -> Unit,
) : AppSheet(R.layout.sheet_allergen_config) {

    // View binding
    private val binding: SheetAllergenConfigBinding by viewBinding(SheetAllergenConfigBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set all check boxes to the currently set values
        binding.apply {
            checkWheat.isChecked = currentAllergenConfig.hasWheat
            checkRye.isChecked = currentAllergenConfig.hasRye
            checkBarley.isChecked = currentAllergenConfig.hasBarley
            checkOat.isChecked = currentAllergenConfig.hasOats
            checkSpelt.isChecked = currentAllergenConfig.hasSpelt
            checkKamut.isChecked = currentAllergenConfig.hasKamut
            checkCrustaceans.isChecked = currentAllergenConfig.hasCrustaceans
            checkEggs.isChecked = currentAllergenConfig.hasEggs
            checkFish.isChecked = currentAllergenConfig.hasFish
            checkPeanuts.isChecked = currentAllergenConfig.hasPeanuts
            checkSoy.isChecked = currentAllergenConfig.hasSoy
            checkDairy.isChecked = currentAllergenConfig.hasDairy
            checkAlmonds.isChecked = currentAllergenConfig.hasAlmonds
            checkHazelnuts.isChecked = currentAllergenConfig.hasHazelnuts
            checkWalnuts.isChecked = currentAllergenConfig.hasWalnuts
            checkCashewNuts.isChecked = currentAllergenConfig.hasCashewNuts
            checkPecans.isChecked = currentAllergenConfig.hasPecans
            checkBrazilNuts.isChecked = currentAllergenConfig.hasBrazilNuts
            checkPistachios.isChecked = currentAllergenConfig.hasPistachios
            checkMacadamia.isChecked = currentAllergenConfig.hasMacadamia
            checkCelery.isChecked = currentAllergenConfig.hasCelery
            checkMustard.isChecked = currentAllergenConfig.hasMustard
            checkSulphides.isChecked = currentAllergenConfig.hasSulphides
            checkLupins.isChecked = currentAllergenConfig.hasLupins
            checkSesame.isChecked = currentAllergenConfig.hasSesame
            checkMolluscs.isChecked = currentAllergenConfig.hasMolluscs
        }

        // Set save button
        binding.buttonSave.setOnClickListener {
            saveAllergenPrefs()
        }
    }

    /**
     * Saves the newly set preferences and closes the sheet.
     */
    private fun saveAllergenPrefs() {

        binding.apply {
            // Save the new preferences
            onUpdate(
                AllergenPreferences.Object(
                    hasWheat = checkWheat.isChecked,
                    hasRye = checkRye.isChecked,
                    hasBarley = checkBarley.isChecked,
                    hasOats = checkOat.isChecked,
                    hasSpelt = checkSpelt.isChecked,
                    hasKamut = checkKamut.isChecked,
                    hasCrustaceans = checkCrustaceans.isChecked,
                    hasEggs = checkEggs.isChecked,
                    hasFish = checkFish.isChecked,
                    hasPeanuts = checkPeanuts.isChecked,
                    hasSoy = checkSoy.isChecked,
                    hasDairy = checkDairy.isChecked,
                    hasAlmonds = checkAlmonds.isChecked,
                    hasHazelnuts = checkHazelnuts.isChecked,
                    hasWalnuts = checkWalnuts.isChecked,
                    hasCashewNuts = checkCashewNuts.isChecked,
                    hasPecans = checkPecans.isChecked,
                    hasBrazilNuts = checkBrazilNuts.isChecked,
                    hasPistachios = checkPistachios.isChecked,
                    hasMacadamia = checkMacadamia.isChecked,
                    hasCelery = checkCelery.isChecked,
                    hasMustard = checkMustard.isChecked,
                    hasSulphides = checkSulphides.isChecked,
                    hasLupins = checkLupins.isChecked,
                    hasSesame = checkSesame.isChecked,
                    hasMolluscs = checkMolluscs.isChecked,
                )
            )
        }

        // Tell the user that saving was successful
        showToast(context, getString(R.string.allergens_config_confirmation))

        // Close the sheet
        dismiss()
    }
}