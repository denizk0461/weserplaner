package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.denizk0461.studip.R
import com.denizk0461.studip.adapter.CanteenOfferPageAdapter
import com.denizk0461.studip.data.showErrorSnackBar
import com.denizk0461.studip.databinding.FragmentCanteenBinding
import com.denizk0461.studip.model.*
import com.denizk0461.studip.sheet.TextSheet
import com.denizk0461.studip.viewmodel.CanteenViewModel
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
     * BUG if the fragment is changed before the refresh is finished, the app crashes with a NPE
     */
    private val binding get() = _binding!!

    // Adapter for the view pager displaying all offers
    private lateinit var viewPagerAdapter: CanteenOfferPageAdapter

    // View model reference for providing access to the database
    private val viewModel: CanteenViewModel by viewModels()

    private var openingHours = ""

    private var dates: List<String> = listOf()

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCanteenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Open the menu for the canteen picker button
        binding.buttonCanteenPicker.setOnClickListener {
            PopupMenu(binding.root.context, binding.buttonCanteenPicker).apply {
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
                    binding.buttonCanteenPicker.text = getCurrentlySelectedCanteenName()

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

        // Set currently selected canteen to the button
        binding.buttonCanteenPicker.text = getCurrentlySelectedCanteenName()

        // Set up button to display info (most likely opening hours)
        binding.buttonInfo.setOnClickListener {
            openBottomSheet(
                TextSheet(
                    getString(
                        R.string.canteen_opening_hours,
                        getCurrentlySelectedCanteenName()
                    ),
                    openingHours
                )
            )
        }

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
            chip.setOnCheckedChangeListener { buttonView, newValue ->
                // Save the preference change to persistent storage
                setPreference(pref, newValue)

                /*
                 * Force an animation on click. This is a subpar method from StackOverflow. Since
                 * it removes the view before immediately adding it back, this method causes a
                 * flicker.
                 * TODO implement a more efficient / better-looking method
                 * TODO order the chips alphabetically?
                 */
                val index = binding.chipsPreference.indexOfChild(buttonView)
                binding.chipsPreference.removeView(buttonView)
                binding.chipsPreference.addView(buttonView, index)
            }
        }

        // Set up the view pager's adapter
        viewPagerAdapter = CanteenOfferPageAdapter(activity as FragmentActivity)

        // Date count is observed to correctly set the amount of pages and the corresponding tabs
        viewModel.getDates().observe(viewLifecycleOwner) { newDates ->

            // Retrieve the dates individually and store them for the tab mediator to use
            dates = newDates.map { it.date }

            // Let the adapter know of the new amount of dates (pages) to display
            viewPagerAdapter.itemCount = dates.size
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
            /*
             * Tell the swipe refresh layout to stop refreshing.
             */
            binding.swipeRefreshLayout.isRefreshing = false

        }, onError = {
            context?.theme?.showErrorSnackBar(
                binding.snackbarContainer,
                getString(R.string.canteen_fetch_error)
            )
            binding.swipeRefreshLayout.isRefreshing = false
        })
    }
}