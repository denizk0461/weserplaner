package com.denizk0461.weserplaner.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.viewBinding
import com.denizk0461.weserplaner.databinding.SheetAllergenConfigBinding
import com.denizk0461.weserplaner.fragment.SettingsFragment
import com.denizk0461.weserplaner.values.AllergenPreferences
import com.denizk0461.weserplaner.viewmodel.AllergenConfigViewModel

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

        // Set up FAB behaviour for parent fragment's FAB if activity is not null
        val parentFragment = activity?.let { activity ->
            // Retrieve parent fragment to access its functions
            val navHostFragment = activity
                .supportFragmentManager
                .fragments[0] as NavHostFragment
            navHostFragment
                .childFragmentManager
                .primaryNavigationFragment as SettingsFragment
        }

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
            // Save newly set preferences
            saveAllergenPrefs()

            /*
             * Send an empty bundle to tell the parent fragment that it should update its view
             * according to the changes made here.
             */
            parentFragment?.setFragmentResult("allergenConfig", Bundle())

            // Tell the user that saving was successful
            parentFragment?.showSnackBar(getString(R.string.allergens_config_confirmation))

            // Close the sheet
            dismiss()
        }

        // Set up close button
        binding.buttonCancel.setOnClickListener {
            // Do nothing and dismiss the sheet
            dismiss()
        }
    }

    /**
     * Saves the newly set preferences.
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
    }
}