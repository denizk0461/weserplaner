package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.denizk0461.studip.adapter.CanteenOfferItemAdapter
import com.denizk0461.studip.adapter.CanteenOfferPageAdapter
import com.denizk0461.studip.databinding.FragmentCanteenBinding
import com.denizk0461.studip.model.*
import com.denizk0461.studip.sheet.AllergenSheet
import com.denizk0461.studip.viewmodel.CanteenViewModel
import com.google.android.material.tabs.TabLayoutMediator

/**
 * User-facing fragment view that displays the canteen offers from the website of the
 * Studierendenwerk Bremen.
 */
class CanteenFragment : AppFragment(), CanteenOfferItemAdapter.OnClickListener {

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

    // Elements in the canteen plan
    private var elements: List<CanteenOffer> = listOf()

    // Count of days displayed in the canteen plan
    private var dateSize: Int = 0

    // Used if no preference has been set or none can be found
    private val emptyPreferenceRegex: String = ".........."

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCanteenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assign a preference value to every button to filter for dietary preferences
        val chipMap = mapOf(
            binding.chipPrefFair to DietaryPreferences.FAIR,
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
                 * TODO order the chips alphabetically
                 */
                val index = binding.chipsPreference.indexOfChild(buttonView)
                binding.chipsPreference.removeView(buttonView)
                binding.chipsPreference.addView(buttonView, index)
            }
        }

        // Set up the view pager's adapter
        viewPagerAdapter = CanteenOfferPageAdapter(listOf(), 0, this)

        // Assign the adapter to the view pager
        binding.viewPager.adapter = viewPagerAdapter

        // Create and attach the tab mediator for the view pager
        createTabLayoutMediator()

        // Set up LiveData observer to refresh the view on update
        viewModel.allOffers.observe(viewLifecycleOwner) { offers ->
            // Update the element list stored in this fragment
            elements = offers

            // Group elements by category
            val groupedElements = offers.groupElements().distinct()

            // Find all dates stored in the database
            val newDates = groupedElements.map { it.date }.distinct() // TODO viewModel.getDates()

            // Update the date count stored in this fragment
            dateSize = newDates.size

            // Update the item list in the view pager's adapter
            viewPagerAdapter.setNewItems(groupedElements, dateSize)

            /*
             * Tell the swipe refresh layout to stop refreshing.
             * TODO if an exception is raised, this will not be fired. This must be changed
             */
            binding.swipeRefreshLayout.isRefreshing = false
        }

        // Set up functions for when the user swipes to refresh the view
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Retrieve new offers from the website(s)
            viewModel.fetchOffers(onRefreshUpdate = { status ->
                // TODO refresh updates
            }, onFinish = {
                createTabLayoutMediator()
//                binding.swipeRefreshLayout.isRefreshing = false
//                createTabLayoutMediator()
            })
        }
    }

    /**
     * Create and attach the object mediating the tabs for the view pager.
     * TODO this is crash-prone
     */
    private fun createTabLayoutMediator() {
        // Fetch all dates from the database
        val dates = viewModel.getDates()

        if (dates.isNotEmpty()) {
            TabLayoutMediator(binding.dayTabLayout, binding.viewPager) { tab, position ->
                tab.text = dates[position].date
            }.attach()

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
        viewPagerAdapter.setNewItems(elements.groupElements().distinct(), dateSize)
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
     * Retrieves the user's dietary preferences and compiles them into a regular expression that can
     * be used to filter out items that don't fulfil the preferences. Example:
     * .......t..|........t.
     * This will retrieve all items that marked as either vegetarian or vegan
     * *
     * @return  regular expression object reflecting the user's preferences
     */
    private fun getPrefRegex(): Regex {
        // Retrieve the user's dietary preference as a string
        val prefs = viewModel.getDietaryPrefs().deconstruct()

        // Return the 'empty' template string if no preference has been set or found
        return if (prefs == emptyPreferenceRegex) {
            Regex(emptyPreferenceRegex)
        } else {
            // Create the variable to insert the regular expression into
            var regexString = ""

            // Create the object to store which preferences need to be met
            val indices = mutableListOf<Int>()

            // Find all preferences that need to be met
            prefs.forEachIndexed { index, c ->
                if (c == DietaryPrefObject.C_TRUE) indices.add(index)
            }

            // Needs to be set to correctly assemble the regular expression
            var isFirst = true

            // Iterate through every preference that has been set
            indices.forEach { index ->
                // Set an OR if more than one preference needs to be met
                if (!isFirst) regexString += '|'

                // Add the expression looking for the single preference to the entire string
                regexString += emptyPreferenceRegex.substring(0 until index) +
                        DietaryPrefObject.C_TRUE +
                        emptyPreferenceRegex.substring(index+1)

                // Ensure that OR statements will be placed between the individual expressions
                isFirst = false
            }

            // Compile the string to a regular expression object
            Regex(regexString)
        }
    }

    /**
     * Groups [CanteenOffer] elements by their categories into [CanteenOfferGroup] elements and
     * filters for the user's dietary preferences.
     *
     * @return  the grouped and filtered elements
     */
    private fun List<CanteenOffer>.groupElements(): List<CanteenOfferGroup> {
        // Get dietary preference regex
        val prefsRegex = getPrefRegex()

        val filteredForPreferences = if (prefsRegex.toString() == emptyPreferenceRegex) {
            // Show all elements and skip filtering if no preference is set
            this
        } else {
            // Filter for dietary preferences
            this.filter {
                prefsRegex.matches(it.dietaryPreferences)
            }
        }

        return filteredForPreferences.map { offer ->
            // Create new group elements to group the already filtered elements by their categories
            CanteenOfferGroup(
                offer.date,
                offer.dateId,
                offer.category,
                offer.canteen,
                filteredForPreferences.filter {
                    it.category == offer.category && it.date == offer.date && it.canteen == offer.canteen
                }.map {
                    // Map the individual items to their respective groups
                    CanteenOfferGroupElement(
                        it.title,
                        it.price,
                        it.dietaryPreferences,
                        it.allergens
                    )
                }
            )
        }
    }

    /**
     * Executed when an item has been clicked.
     *
     * @param offer item that has been clicked
     */
    override fun onClick(offer: CanteenOfferGroupElement) {
        openBottomSheet(AllergenSheet(offer))
    }

    /**
     * Executed when an item has been long-pressed.
     *
     * @param offer item that has been long-pressed
     * @return      whether the long press was successful
     */
    override fun onLongClick(offer: CanteenOfferGroupElement): Boolean {
        return false
    }
}