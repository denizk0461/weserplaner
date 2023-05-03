package com.denizk0461.studip.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.denizk0461.studip.R
import com.denizk0461.studip.data.showToast
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetAllergenConfigBinding
import com.denizk0461.studip.model.AllergenPreferences
import com.denizk0461.studip.viewmodel.AllergenConfigViewModel

/**
 * Configuration sheet for the user to pick substances they are allergic against. Offers containing
 * these allergens will be hidden.
 */
class AllergenConfigSheet : AppSheet(R.layout.sheet_allergen_config) {

    // View binding
    private val binding: SheetAllergenConfigBinding by viewBinding(SheetAllergenConfigBinding::bind)

    // View model
    private val viewModel: AllergenConfigViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve allergens as currently set by the user from view model
        val currentAllergenConfig = AllergenPreferences.construct(
            viewModel.preferenceAllergenConfig
        )

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

        // Set up close button
        binding.buttonCancel.setOnClickListener {
            // Do nothing and dismiss the sheet
            dismiss()
        }
    }

    /**
     * Saves the newly set preferences and closes the sheet.
     */
    private fun saveAllergenPrefs() {

        binding.apply {
            // Save the new preferences
            viewModel.preferenceAllergenConfig = AllergenPreferences.Object(
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
            ).deconstruct()
        }

        // Tell the user that saving was successful
        showToast(context, getString(R.string.allergens_config_confirmation))

        // Close the sheet
        dismiss()
    }
}