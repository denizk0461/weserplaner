package com.denizk0461.weserplaner.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.denizk0461.weserplaner.BuildConfig
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.activity.FetcherActivity
import com.denizk0461.weserplaner.activity.ImageActivity
import com.denizk0461.weserplaner.data.getTextSheet
import com.denizk0461.weserplaner.data.showSnackBar
import com.denizk0461.weserplaner.data.showToast
import com.denizk0461.weserplaner.databinding.FragmentSettingsBinding
import com.denizk0461.weserplaner.model.FormattedDate
import com.denizk0461.weserplaner.sheet.AllergenConfigSheet
import com.denizk0461.weserplaner.viewmodel.SettingsViewModel
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * User-facing fragment view that is used to change app settings.
 */
class SettingsFragment : AppFragment<FragmentSettingsBinding>() {

    // View model reference for providing access to the database
    private val viewModel: SettingsViewModel by viewModels()

    // How many long clicks the user has done on the licences button
    private var licencesLongClick = 1

    // Click counter on the app version button
    private var appVersionClick = 1

    // Whether the user has set the text of the allergen setting to its alt text
    private var hasSetAllergensAltText = false

    // 222
    private val mysteryLink = "https://www.youtube.com/watch?v=nhIQMCXJzLI"

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- schedule settings --- //

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

        // --- canteen offer settings --- //

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

        // Set text according to how many allergens the user has set
        binding.allergensConfigSubtitle.text = getAllergensConfigCountText()

        // Set up switch for displaying allergens
        binding.switchAllergens.apply {
            isChecked = viewModel.preferenceAllergen
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceAllergen = newValue
            }
        }

        // Change text on allergens setting
        binding.settingsAllergensSubtitleView.setOnLongClickListener {
            if (hasSetAllergensAltText) {
                binding.settingsAllergensTitleView.text = getString(R.string.settings_allergens_title)
                binding.settingsAllergensSubtitleView.text = getString(R.string.settings_allergens_subtitle)
                hasSetAllergensAltText = false
            } else {
                binding.settingsAllergensTitleView.text = getString(R.string.settings_allergens_title_alt)
                binding.settingsAllergensSubtitleView.text = getString(R.string.settings_allergens_subtitle_alt)
                hasSetAllergensAltText = true
            }
            true
        }

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

        // --- miscellaneous settings --- //

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

        // TODO Set up dropdown menu for picking a date format
//        val dateAdapter = DropdownAdapter(
//            context,
//            context.resources.getStringArray(R.array.settings_date_format_items).toList(),
//        )
//
//        binding.autoCompleteFont.setAdapter(dateAdapter)
//        binding.autoCompleteFont.setText(
//            dateAdapter.getItem(storage.getTypefaceIndex()).toString(),
//            false,
//        )
//
//        binding.autoCompleteFont.setOnItemClickListener { _, _, position, _ ->
//            storage.setTypefaceIndex(position)
//            binding.autoCompleteFont.setText(dateAdapter.getItem(position), false)
//        }

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
                    getString(
                        R.string.sheet_licences_content,
                        Calendar.getInstance().get(Calendar.YEAR).toString(),
                    ),
                )
            )
        }

        // Set long click listener for licences button to unlock experimental settings
        binding.buttonLicences.setOnLongClickListener {
            // Return false if the experimental settings are already enabled
            if (viewModel.preferenceExperimentalSettingsEnabled) return@setOnLongClickListener false

            // Unlock experimental settings after 3 long presses
            if (licencesLongClick > 3) {
//                openBottomSheet(DevCodeSheet())
                licencesLongClick = 0
                viewModel.preferenceExperimentalSettingsEnabled = true
                binding.cardExperimentalSettings.visibility = View.VISIBLE
                showSnackBar(
                    getString(R.string.settings_unlocked_experiments),
                )
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
            "${BuildConfig.VERSION_NAME}-${if (BuildConfig.DEBUG) "dev" else "release"}"

        // --- experimental settings --- //

        // Set visibility of experimental settings depending on whether they have been unlocked
        binding.cardExperimentalSettings.visibility = if (
            viewModel.preferenceExperimentalSettingsEnabled
        ) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.buttonHideExperiments.setOnClickListener {
            viewModel.preferenceExperimentalSettingsEnabled = false
            binding.cardExperimentalSettings.visibility = View.GONE
            showSnackBar(
                getString(R.string.settings_hide_experiments),
            )
        }

        // Set build version code and time text
        @SuppressLint("SetTextI18n")
        binding.buildTimeText.text = "v${BuildConfig.VERSION_CODE} | ${
            FormattedDate(BuildConfig.BUILD_TIME_MILLIS).commaSeparatedString(context)
        }"

        // Set up switch for showing beta screens
        binding.switchBetaNav.apply {
            isChecked = viewModel.preferenceBetaScreensEnabled
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceBetaScreensEnabled = newValue
            }
        }

        binding.buttonDevCodes.setOnClickListener {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            when (binding.inputDevCode.text.toString().uppercase()) {
                "U1RST0JF".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS90S2k5Wi1mNnFYNA==".d64)
                "U1BJUklU".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9XN25lMzlEU05TZw==".d64)
                "Q0xPVURT".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9rbUJ6YTRhNTB2dw==".d64)
                "NjQxODJL".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9EbTFyZ285UHBUcw==".d64)
                "U0FJTE9S".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9QUHlDYXZ6OG5NWQ==".d64)
                "SE9ORVNU".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9DSEg0OE5LUEFraw==".d64)
                "QkJVT0s/".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS90Rm1PMm1TY0tHNA==".d64)
                "SUhUU0Mq".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS85bXZ4SVdhWHZuWQ==".d64)
                "SU5TQU5F".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS90NE9kYTlUZFYwbw==".d64)
                "SE9SSVpO".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9SUVpUUy1CWjhtcw==".d64)
                "TUFSR0Uh".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS8tbHFxRHZXRjQ1dw==".d64)
                "UE9XRUxM".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9XUGMtVkVxQlBISQ==".d64)
                "Q0FMTE1F".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS8xbTV1WnJNMnktMA==".d64)
                "SkFNSVJP".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9fMVZoT2c0N3ozNA==".d64)
                "TE9ORUxZ".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9uLURFNHNfc3d5Zw==".d64)
                "QkVBVVRZ".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9vbVQ1ckttVTVfaw==".d64)
                "Uk9NQ09N".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9HUW1nUG5uZjE4WQ==".d64)
                "Tk9DQVNI".d64 -> launchLink(listOf(
                    "aHR0cHM6Ly95b3V0dS5iZS9lRzh3bkx2NTlPbw==",
                    "aHR0cHM6Ly95b3V0dS5iZS9RSzBZLUQ1bVpaaw==",
                ).random().d64)
                "Qk9KQUNL".d64 -> {
                    startActivity(Intent(context, ImageActivity::class.java).also { intent ->
                        val bundle = Bundle()
                        bundle.putString("img", "Qk9KQUNL".d64.lowercase())
                        intent.putExtras(bundle)
                    })
                }
                "QkNFTExT".d64 -> {
                    startActivity(Intent(context, ImageActivity::class.java).also { intent ->
                        val bundle = Bundle()
                        bundle.putString("img", "QkNFTExT".d64.lowercase())
                        intent.putExtras(bundle)
                    })
                }
                "NEVENT" -> { // nuke events
                    viewModel.nukeEvents()
                    showToast(context, "Nuked all events")
                }
                "NCANTE" -> { // nuke canteens
                    viewModel.nukeOfferCanteens()
                    showToast(context, "Nuked all canteens")
                }
                "NDATES" -> { // nuke dates
                    viewModel.nukeOfferDates()
                    showToast(context, "Nuked all dates")
                }
                "NCATEG" -> { // nuke categories
                    viewModel.nukeOfferCategories()
                    showToast(context, "Nuked all categories")
                }
                "NITEMS" -> { // nuke items
                    viewModel.nukeOfferItems()
                    showToast(context, "Nuked all items")
                }
                "NEVERY" -> { // nuke everything
                    viewModel.nukeEverything()
                    showToast(context, "Nuked everything")
                }
                "NFIRST" -> { // set first launch preference to true
                    viewModel.preferenceFirstLaunch = true
                    showToast(context, "First launch flag cleared")
                }
                else -> showSnackBar(
                    getString(R.string.settings_dev_codes_invalid)
                )
            }
        }
    }

    /**
     * Launches a web link.
     *
     * @param link  link to open
     */
    private fun launchLink(link: String) {
        // Let the user know they accomplished something with their life
        showToast(context, "✨")

        // Give the user a slight dopamine boost
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    /**
     * Creates and shows a snack bar.
     *
     * @param text  content to display in the snack bar
     */
    fun showSnackBar(text: String) {
        context.theme.showSnackBar(binding.coordinatorLayout, text)
    }

    /**
     * Decodes Base64 to a regular string.
     *
     * @receiver    encoded string to decode
     * @return      decoded string
     */
    private val String.d64: String get() = String(
        Base64.decode(this, Base64.DEFAULT),
        StandardCharsets.UTF_8
    )

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
     * Retrieves the appropriate text to display in the allergen config setting to show the user how
     * many allergens they have checked. Text depends on how many allergens have been checked.
     *
     * @return localised string with the count of checked allergens inserted
     */
    private fun getAllergensConfigCountText(): String = when (viewModel.preferenceAllergenConfigCount) {
        0 -> getString(R.string.allergens_config_subtitle_zero)
        1 -> getString(R.string.allergens_config_subtitle_one)
        else -> getString(R.string.allergens_config_subtitle_more, viewModel.preferenceAllergenConfigCount)
    }
}