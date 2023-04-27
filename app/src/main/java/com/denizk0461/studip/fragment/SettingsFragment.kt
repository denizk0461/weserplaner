package com.denizk0461.studip.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.denizk0461.studip.BuildConfig
import com.denizk0461.studip.R
import com.denizk0461.studip.activity.FetcherActivity
import com.denizk0461.studip.databinding.FragmentSettingsBinding
import com.denizk0461.studip.sheet.DevCodeSheet
import com.denizk0461.studip.sheet.TextSheet
import com.denizk0461.studip.viewmodel.SettingsViewModel
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

    // Click counter on the app version button
    private var appVersionClick = 0

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
        binding.switchHighlight.apply {
            isChecked = viewModel.preferenceCourseHighlighting
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceCourseHighlighting = newValue
            }
        }

        // Set up switch for displaying allergens
        binding.switchAllergens.apply {
            isChecked = viewModel.preferenceAllergen
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceAllergen = newValue
            }
        }

        // Set up switch for launching the app with a specific view
        binding.switchLaunchCanteen.apply {
            isChecked = viewModel.preferenceLaunchCanteen
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceLaunchCanteen = newValue
            }
        }

        // Set up switch for crash report opt-in
        binding.switchCrashlytics.apply {
            isChecked = viewModel.preferenceDataHandling
            setOnCheckedChangeListener { _, newValue ->
                viewModel.preferenceDataHandling = newValue
            }
        }

        // Set click listener for the data handling dialogue
        binding.buttonDataHandling.setOnClickListener {
            openBottomSheet(TextSheet(
                getString(R.string.settings_data_sheet_header),
                    getString(R.string.settings_data_sheet_content),
            ))
        }

        // Set click listener for showing licences dialogue
        binding.buttonLicences.setOnClickListener {
            openBottomSheet(
                TextSheet(
                    getString(R.string.sheet_licences_header),
                    getString(R.string.sheet_licences_content),
                )
            )
        }

        // Set long click listener for licences button to open dev code sheet
        binding.buttonLicences.setOnLongClickListener {
            openBottomSheet(DevCodeSheet(
                viewModel::nukeEvents,
                viewModel::nukeOfferItems,
                viewModel::nukeOfferCategories,
                viewModel::nukeOfferCanteens,
                viewModel::nukeOfferDates,
                viewModel::nukeEverything,
            ))
            true
        }

        // Set click listener for the app version button
        binding.buttonAppVersion.setOnClickListener {
            when (appVersionClick) {
                22 -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mysteryLink)))
                    appVersionClick = 0
                }
                else -> appVersionClick += 1
            }
        }

        /*
         * Display the app's version as set in the build.gradle. Also display if the app is a
         * development version, and if so, display build timestamp.
         */
        @SuppressLint("SetTextI18n")
        binding.appVersionText.text =
            "${BuildConfig.VERSION_NAME}-${
                if (BuildConfig.DEBUG) 
                    "dev-[${SimpleDateFormat("yyyy-MM-dd, HH:mm:ss.SSS", Locale.GERMANY).format(Date(BuildConfig.TIMESTAMP))}]" 
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
}