package com.denizk0461.weserplaner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.adapter.CanteenOfferPageAdapter
import com.denizk0461.weserplaner.data.getTextSheet
import com.denizk0461.weserplaner.data.setRainbowProgressCircle
import com.denizk0461.weserplaner.data.showErrorSnackBar
import com.denizk0461.weserplaner.databinding.FragmentCanteenBinding
import com.denizk0461.weserplaner.model.*
import com.denizk0461.weserplaner.viewmodel.CanteenViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayoutMediator

/**
 * User-facing fragment view that displays the canteen offers from the website of the
 * Studierendenwerk Bremen.
 */
class CanteenFragment : AppFragment() {

    // Nullable view binding reference
    private var _binding: FragmentCanteenBinding? = null

    /*
     * Non-null reference to the view binding. This property is only valid between onCreateView and
     * onDestroyView.
     */
    private val binding get() = _binding!!

    // Adapter for the view pager displaying all offers
    private lateinit var viewPagerAdapter: CanteenOfferPageAdapter

    // View model reference for providing access to the database
    private val viewModel: CanteenViewModel by viewModels()

    /**
     * Locally stored dates to populate the TabLayout with.
     */
    private var dates: List<String> = listOf()

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCanteenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set progress circle colours
        binding.swipeRefreshLayout.setRainbowProgressCircle()

        // Set currently selected canteen to the button
        binding.appTitleBar.text = getString(
            R.string.title_canteen_template,
            getCurrentlySelectedCanteenName(),
        )
        binding.appTitleBar.isSelected = true

        binding.buttonNotifications.setOnClickListener {
            openBottomSheet(getTextSheet(
                getString(R.string.canteen_news_sheet_title),
                contentId = TextSheetContentId.NEWS,
            ))
        }

        // Create badge
        val badge = BadgeDrawable.create(context)
        badge.isVisible = true
        // Attach badge to button
        BadgeUtils.attachBadgeDrawable(badge, binding.buttonNotifications)

        // Assign a preference value to every button to filter for dietary preferences
        val chipMap = mapOf(
            binding.chipPrefFair to DietaryPreferences.WELFARE,
            binding.chipPrefFish to DietaryPreferences.FISH,
            binding.chipPrefPoultry to DietaryPreferences.POULTRY,
            binding.chipPrefLamb to DietaryPreferences.LAMB,
            binding.chipPrefVital to DietaryPreferences.VITAL,
            binding.chipPrefBeef to DietaryPreferences.BEEF,
            binding.chipPrefPork to DietaryPreferences.PORK,
            binding.chipPrefVegetarian to DietaryPreferences.VEGETARIAN,
            binding.chipPrefVegan to DietaryPreferences.VEGAN,
            binding.chipPrefGame to DietaryPreferences.GAME,
        )

        // Set up every chip
        chipMap.forEach { (chip, pref) ->
            // Set chip to be checked if user has set this preference before
            chip.isChecked = getPreference(pref)

            // Set a listener for when the user clicks the chip
            chip.setOnCheckedChangeListener { _, newValue ->
                // Save the preference change to persistent storage
                setPreference(pref, newValue)
            }
        }

        // Set up floating action button for viewing the opening hours
        binding.fabOpeningHours.setOnClickListener {
            openBottomSheet(
                getTextSheet(
                    getString(
                        R.string.canteen_opening_hours,
                        getCurrentlySelectedCanteenName()
                    ),
                    contentId = TextSheetContentId.OPENING_HOURS,
                )
            )
        }

        // Set up floating action button for switching the canteen
        binding.fabSwitchCanteen.setOnClickListener {
            PopupMenu(binding.root.context, binding.fabSwitchCanteen).apply {
                setOnMenuItemClickListener { item ->
                    viewModel.preferenceCanteen = when (item?.itemId) {
                        R.id.mensa_uni -> 0
                        R.id.cafe_central -> 1
                        R.id.mensa_nw1 -> 2
                        R.id.cafeteria_gw2 -> 3
                        R.id.mensa_neustadt -> 4
                        R.id.mensa_werder -> 5
                        R.id.mensa_airport -> 6
                        R.id.mensa_bhv -> 7
                        R.id.cafeteria_bhv -> 8
                        R.id.mensa_hfk -> 9
                        else -> 0
                    }
                    // Set newly selected canteen to the button
//                    binding.buttonCanteenPicker.text = getCurrentlySelectedCanteenName()
                    binding.appTitleBar.text = getString(
                        R.string.title_canteen_template,
                        getCurrentlySelectedCanteenName(),
                    )

                    // Display to the user that the canteen plan will be refreshed
                    binding.swipeRefreshLayout.isRefreshing = true

                    // Refresh the canteen menu for the newly selected canteen
                    refresh()
                    true
                }
                inflate(R.menu.menu_canteens)
                show()
            }
        }

        // Set up floating action button for refreshing the canteen offers
        binding.fabRefreshOffers.setOnClickListener {
            // Display to the user that a refresh is in progress
            binding.swipeRefreshLayout.isRefreshing = true

            // Refresh the offers
            refresh()
        }

        // Set up the view pager's adapter
        viewPagerAdapter = CanteenOfferPageAdapter(
            childFragmentManager,
            lifecycle,
        )

        // Date count is observed to correctly set multiple elements that depend on up-to-date data
        viewModel.getDates().observe(viewLifecycleOwner) { newDates ->

            // Retrieve the dates individually and store them for the tab mediator to use
            dates = newDates.map { it.date }

            // Let the adapter know of the new amount of dates (pages) to display
            viewPagerAdapter.itemCount = dates.size

            // Retrieve further information about this canteen from the database
//            viewModel.getCanteen().also { canteen ->
                // Set visibility of the notifications icon depending on whether news are available
//                badge.isVisible = info.news.isNotBlank()
//            }
        }

        // Assign the adapter to the view pager
        binding.viewPager.adapter = viewPagerAdapter

        // Set up TabLayoutMediator to populate tabs
        TabLayoutMediator(binding.dayTabLayout, binding.viewPager) { tab, position ->
            tab.text = dates[position]
        }.attach()

        // Set up functions for when the user swipes to refresh the view
        binding.swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    // Invalidate the view binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Updates a given preference and refreshes the view.
     *
     * @param pref      preference to be updated
     * @param newValue  value to set the preference to
     */
    private fun setPreference(pref: DietaryPreferences, newValue: Boolean) {
        // Update the preference
        viewModel.setPreference(pref, newValue)

        // Update the view
        viewModel.registerDietaryPreferencesUpdate()
    }

    /**
     * Retrieves a single user-specified dietary preference.
     *
     * @param pref  the dietary preference that should be retrieved
     * @return      whether the preference needs to be met
     */
    private fun getPreference(pref: DietaryPreferences): Boolean =
        viewModel.getPreference(pref)

    /**
     * Retrieves the localised string for the canteen selected by the user.
     *
     * @return  the localised canteen name
     */
    private fun getCurrentlySelectedCanteenName(): String = getString(
        when (viewModel.preferenceCanteen) {
            0 -> R.string.mensa_uni
            1 -> R.string.cafe_central
            2 -> R.string.mensa_nw1
            3 -> R.string.cafeteria_gw2
            4 -> R.string.mensa_neustadt
            5 -> R.string.mensa_werder
            6 -> R.string.mensa_airport
            7 -> R.string.mensa_bhv
            8 -> R.string.cafeteria_bhv
            9 -> R.string.mensa_hfk
            else -> R.string.mensa_uni
        }
    )

    /**
     * Downloads the canteen offers and refreshes them in the app.
     */
    private fun refresh() {
        // Retrieve new offers from the website(s)
        viewModel.fetchOffers(viewModel.preferenceCanteen, onFinish = {
            // Check if the fragment is still active
            if (_binding != null) {
                /*
                 * Tell the swipe refresh layout to stop refreshing.
                 */
                binding.swipeRefreshLayout.isRefreshing = false
            }

        }, onError = {
            // Check if the fragment is still active
            if (_binding != null) {
                // Tell the user that an error occurred
                context.theme?.showErrorSnackBar(
                    binding.snackbarContainer,
                    getString(R.string.canteen_fetch_error)
                )
                /*
                 * Tell the swipe refresh layout to stop refreshing.
                 */
                binding.swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    override fun onPause() {
        if (_binding != null) {
            /*
             * Stop the refreshing animation if the user switches fragments.
             */
            binding.swipeRefreshLayout.isRefreshing = false
        }
        super.onPause()
    }
}