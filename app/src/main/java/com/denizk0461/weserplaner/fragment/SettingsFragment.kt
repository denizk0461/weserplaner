package com.denizk0461.weserplaner.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.denizk0461.weserplaner.BuildConfig
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.activity.FetcherActivity
import com.denizk0461.weserplaner.activity.ImageActivity
import com.denizk0461.weserplaner.data.getTextSheet
import com.denizk0461.weserplaner.data.showToast
import com.denizk0461.weserplaner.databinding.FragmentSettingsBinding
import com.denizk0461.weserplaner.sheet.AllergenConfigSheet
import com.denizk0461.weserplaner.sheet.DevCodeSheet
import com.denizk0461.weserplaner.viewmodel.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * User-facing fragment view that is used to change app settings.
 */
class SettingsFragment : AppFragment() {

    // Nullable view binding reference
    private var _binding: FragmentSettingsBinding? = null

    /*
     * Non-null reference to the view binding. This property is only valid between onCreateView and
     * onDestroyView.
     */
    private val binding get() = _binding!!

    // View model reference for providing access to the database
    private val viewModel: SettingsViewModel by viewModels()

    // How many long clicks the user has done on the licences button
    private var licencesLongClick = 1

    // Click counter on the app version button
    private var appVersionClick = 1

    // 222
    private val mysteryLink = "https://www.youtube.com/watch?v=nhIQMCXJzLI"

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Launch the Stud.IP schedule fetcher activity
        binding.buttonRefreshSchedule.setOnClickListener {
            launchWebView()
        }

        // Set up switch for highlighting the next course in the schedule
        binding.switchCurrentDay.apply {
            isChecked = viewModel.preferenceCurrentDay
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceCurrentDay = newValue
            }
        }

        // Set up switch for highlighting the next course in the schedule
        binding.switchHighlight.apply {
            isChecked = viewModel.preferenceCourseHighlighting
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceCourseHighlighting = newValue
            }
        }

        // Set up button to set allergens
        binding.buttonAllergensConfig.setOnClickListener {
            openBottomSheet(
                AllergenConfigSheet().also {
                    /*
                     * Receive fragment result to update the text view.
                     * TODO doesn't work after configuration change (change into or out of multi
                     *  window)
                     */
                    setFragmentResultListener("allergenConfig") { _, _ ->
                        // Update how many allergens the user set
                        binding.allergensConfigSubtitle.text = getAllergensConfigCountText()
                    }
                }
            )
        }

        binding.allergensConfigSubtitle.text = getAllergensConfigCountText()

        // Set up switch for displaying allergens
        binding.switchAllergens.apply {
            isChecked = viewModel.preferenceAllergen
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceAllergen = newValue
            }
        }

        // Set up switch for launching the app with a specific view
//        binding.switchLaunchCanteen.apply {
//            isChecked = viewModel.preferenceLaunchCanteen
//            setOnCheckedChangeListener { _, newValue ->
//                viewModel.preferenceLaunchCanteen = newValue
//            }
//        }

        // Set up button toggle group for changing prices displayed for the canteen offers
        binding.togglePricingGroup.apply {
            // Check the pricing that is currently selected
            check(when (viewModel.preferencePricing) {
                1 -> R.id.toggle_pricing_employee
                else -> R.id.toggle_pricing_student // 0
            })

            // Set up checked listener for toggle group
            addOnButtonCheckedListener { _, checkedId, isChecked ->
                /*
                 * Check if the button that is changed is the checked button. If this is not checked
                 * for, the listener will simply loop through this when-statement with all buttons
                 * that had their state changed somehow, and save the ID of the last one that had
                 * its state changed.
                 */
                if (isChecked) {
                    // Save new preference
                    viewModel.preferencePricing = when (checkedId) {
                        R.id.toggle_pricing_employee -> 1
                        else -> 0 // R.id.toggle_pricing_student
                    }
                }
            }
        }

        // Set up button toggle group for changing default fragment
        binding.toggleLaunchFragmentGroup.apply {
            // Check the fragment that is currently selected
            check(when (viewModel.preferenceLaunchFragment) {
                1 -> R.id.toggle_launch_fragment_canteen
                else -> R.id.toggle_launch_fragment_schedule // 0
            })

            // Set up checked listener for toggle group
            addOnButtonCheckedListener { _, checkedId, isChecked ->
                /*
                 * Check if the button that is changed is the checked button. If this is not checked
                 * for, the listener will simply loop through this when-statement with all buttons
                 * that had their state changed somehow, and save the ID of the last one that had
                 * its state changed.
                 */
                if (isChecked) {
                    // Save new preference
                    viewModel.preferenceLaunchFragment = when (checkedId) {
                        R.id.toggle_launch_fragment_canteen -> 1
                        else -> 0 // R.id.toggle_launch_fragment_schedule
                    }
                }
            }
        }

        // Set up switch for colouring dietary preferences
        binding.switchPrefColour.apply {
            isChecked = viewModel.preferenceColour
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceColour = newValue
            }
        }

        /*
         * Set up switch for crash report opt-in, NOTE: currently not shown as Crashlytics is not
         * implemented!
         */
        binding.switchCrashlytics.apply {
            isChecked = viewModel.preferenceDataHandling
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceDataHandling = newValue
            }
        }

        // Set click listener for the data handling dialogue
        binding.buttonDataHandling.setOnClickListener {
            openBottomSheet(
                getTextSheet(
                getString(R.string.settings_data_sheet_header),
                    getString(R.string.settings_data_sheet_content),
                )
            )
        }

        // Set click listener for showing licences dialogue
        binding.buttonLicences.setOnClickListener {
            openBottomSheet(
                getTextSheet(
                    getString(R.string.sheet_licences_header),
                    getString(R.string.sheet_licences_content),
                )
            )
        }

        // Set long click listener for licences button to open dev code sheet
        binding.buttonLicences.setOnLongClickListener {
            if (licencesLongClick > 3) {
                openBottomSheet(DevCodeSheet())
            } else {
                licencesLongClick += 1
            }
            true
        }

        val cheesyWisdoms: MutableList<String> = resources
            .getStringArray(R.array.cheesy_wisdoms)
            .toList()
            .shuffled()
            .toMutableList()

        // Set click listener for the app version button
        binding.buttonAppVersion.setOnClickListener {
            when (appVersionClick) {
                4 -> {
                    startActivity(Intent(context, ImageActivity::class.java).also { intent ->
                        val bundle = Bundle()
                        bundle.putString("img", "garbage")
                        intent.putExtras(bundle)
                    })
                }
                20 -> showToast(context, getString(R.string.settings_app_version_15))
                35 -> showToast(context, getString(R.string.settings_app_version_20))
                50 -> showToast(context, getString(R.string.settings_app_version_30))
                222 -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mysteryLink)))
                }
                254 -> { // after 222 has been reached, this loops every 32 clicks
                    // Show the next cheesy wisdom
                    showToast(context, cheesyWisdoms[0])

                    // Remove the cheesy wisdom from the list
                    cheesyWisdoms.removeAt(0)

                    // Get new cheesy wisdoms if cheesy wisdoms have run out
                    if (cheesyWisdoms.isEmpty()) {
                        cheesyWisdoms.addAll(resources
                            .getStringArray(R.array.cheesy_wisdoms)
                            .toList()
                            .shuffled()
                        )
                    }

                    /*
                     * Reset to 222 to make the user click 16 times again before the next cheesy
                     * wisdom is shown.
                     */
                    appVersionClick = 222
                }
            }

            // Increment click counter
            appVersionClick += 1
        }

        /*
         * Display the app's version as set in the build.gradle. Also display if the app is a
         * development version, and if so, display build timestamp.
         */
        @SuppressLint("SetTextI18n")
        binding.appVersionText.text =
            "${BuildConfig.VERSION_NAME}-${
                if (BuildConfig.DEBUG) 
                    "dev-[${SimpleDateFormat("yyyy-MM-dd, HH:mm:ss.SSS", Locale.GERMANY)
                        .format(Date(BuildConfig.TIMESTAMP))}]" 
                else 
                    "release"
            }"
    }

    // Invalidate the view binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Launch the Stud.IP schedule fetcher activity.
     */
    private fun launchWebView() {
        startActivity(Intent(context, FetcherActivity::class.java))
    }

    /**
     * Retrieves the text displayed in the allergen config setting to show the user how many
     * allergens they have checked.
     *
     * @return localised string with the count of checked allergens inserted
     */
    private fun getAllergensConfigCountText(): String = getString(
        R.string.allergens_config_subtitle,
        viewModel.preferenceAllergenConfigCount
    )
}