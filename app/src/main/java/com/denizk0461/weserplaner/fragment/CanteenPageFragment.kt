package com.denizk0461.weserplaner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denizk0461.weserplaner.adapter.CanteenOfferItemAdapter
import com.denizk0461.weserplaner.adapter.StudIPEventPageAdapter
import com.denizk0461.weserplaner.databinding.RecyclerViewBinding
import com.denizk0461.weserplaner.model.CanteenOffer
import com.denizk0461.weserplaner.model.CanteenOfferGroup
import com.denizk0461.weserplaner.model.CanteenOfferGroupElement
import com.denizk0461.weserplaner.model.DietaryPreferences
import com.denizk0461.weserplaner.sheet.AllergenSheet
import com.denizk0461.weserplaner.viewmodel.CanteenPageViewModel

/**
 * Fragment that is instantiated by [StudIPEventPageAdapter] to display individual days' pages and
 * their events.
 */
class CanteenPageFragment : AppFragment(), CanteenOfferItemAdapter.OnClickListener {

    // Nullable view binding reference
    private var _binding: RecyclerViewBinding? = null

    /*
     * Non-null reference to the view binding. This property is only valid between onCreateView and
     * onDestroyView.
     */
    private val binding get() = _binding!!

    // View model reference for providing access to the database
    private val viewModel: CanteenPageViewModel by viewModels()

    // Recycler view adapter
    private lateinit var recyclerViewAdapter: CanteenOfferItemAdapter

    // List of offers, stored locally to re-set them upon dietary preference update
    private val offerList: MutableList<CanteenOffer> = mutableListOf()

    /**
     * Current day that is used to show only events of a given day (0 = Monday, 4 = Friday)
     */
    private var currentDay: Int = -1

    // Instantiate the view binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Retrieve current day to fetch items specifically for that day
        currentDay = arguments?.getInt("currentDay") ?: -1

        // Inflate view binding
        _binding = RecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up recycler view adapter
        recyclerViewAdapter = CanteenOfferItemAdapter(
            this,
            viewModel.preferenceAllergen,
            viewModel.preferenceColour,
        )

        binding.recyclerView.apply {
            // Set page to scroll vertically
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )

            adapter = recyclerViewAdapter

            // Animate creation of new page
            scheduleLayoutAnimation()

            // Observe offers and re-set them if they change
            viewModel.getOffersByDay(currentDay).observe(viewLifecycleOwner) { offers ->
                // Refresh temporarily stored offer list
                offerList.clear()
                offerList.addAll(offers)

                // Set new data
                recyclerViewAdapter.setData(offers)
            }
        }

        // Set up FAB behaviour for parent fragment's FAB if activity is not null
        activity?.let { activity ->
            // Retrieve parent fragment to access its functions
            val navHostFragment = activity
                .supportFragmentManager
                .fragments[0] as NavHostFragment
            val parentFragment = navHostFragment
                .childFragmentManager
                .primaryNavigationFragment as CanteenFragment

            // Set up scroll change listener to shrink and extend FAB accordingly
            binding.recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                // Calculate the vertical scroll difference
                val dy = scrollY - oldScrollY
                if (dy > 0) {
                    // If scrolling down, shrink the FAB
                    parentFragment.shrinkFab()
                } else if (dy < 0) {
                    // If scrolling up, extend the FAB
                    parentFragment.extendFab()
                }
            }
        }

        /*
         * Observe dietary preference updates and re-set the items if a change is detected to force
         * an update.
         */
        viewModel.dietaryPreferencesUpdate.observe(viewLifecycleOwner) {
            // Re-set data
            recyclerViewAdapter.setData(offerList)
        }
    }

    private fun CanteenOfferItemAdapter.setData(offers: List<CanteenOffer>) {
        /*
         * Filter items that contain user-selected allergens or user-excluded dietary
         * preferences.
         */
        val filteredOffers = offers.filterElements()

        // Put elements into groups
        val groupedOffers = filteredOffers.groupElements().distinct()

        val difference = offers.size - filteredOffers.size

        val newOffers = if (groupedOffers.isEmpty() && difference > 0) {
            listOf(CanteenOfferGroup(
                "ALL\$HIDDEN", 0, "", "", listOf(),
            ))
        } else groupedOffers

        // Set new items into the adapter
        setNewData(
            newOffers,
            // Difference is calculated from the unfiltered and filtered data sets
            difference,
        )
    }

    /**
     * Groups [CanteenOffer] elements by their categories into [CanteenOfferGroup] elements and
     * filters for the user's dietary preferences.
     *
     * @return  the grouped and filtered elements
     */
    private fun List<CanteenOffer>.groupElements(): List<CanteenOfferGroup> {

        return map { (_, date, dateId, category, _, canteen, _, _, _, _, _) ->
            // Create new group elements to group the already filtered elements by their categories
            CanteenOfferGroup(
                date,
                dateId,
                category,
                canteen,
                filter {
                    it.category == category && it.canteen == canteen
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
     * Filters the list of canteen offers by dietary and allergen preferences.
     *
     * @return  filtered list of offers
     */
    private fun List<CanteenOffer>.filterElements(): List<CanteenOffer> {

        // Get dietary preference regex
        val prefsRegex = getPrefRegex()
        val allergenPrefs = viewModel.preferenceAllergenConfig
        val allergenFilteredList = mutableListOf<CanteenOffer>()

        // Filter offers not conforming to the dietary preferences set by the user
        val newList = if (prefsRegex.toString() == DietaryPreferences.TEMPLATE_EMPTY) {
            // Show all elements and skip filtering if no preference is set
            this
        } else {
            // Filter for dietary preferences
            this.filter {
                prefsRegex.matches(it.dietaryPreferences)
            }
        }

        // Filter offers containing allergens the user wishes to have hidden
        return if (allergenPrefs.isBlank()) {
            // If no preferences have been set by the user, skip the checks
            newList
        } else {
            // If the user has set allergens, check each item individually
            newList.forEach outer@{ item ->

                // If the item in question doesn't contain any allergens, skip the check
                if (item.allergens.isBlank()) {
                    // Add the item to the new list directly
                    allergenFilteredList.add(item)
                } else {
                    // Check each individual allergen in the offering
                    item.allergens.split(",").forEach inner@{ allergen ->

                        // If an allergen can be found in the user-excluded list, stop checking
                        if (allergenPrefs.contains(allergen)) {
                            return@outer
                        }
                    }
                    // If no allergens in the offering are present in the exclusion list, add it
                    allergenFilteredList.add(item)
                }
            }
            // Return the allergen-filtered list
            allergenFilteredList
        }
    }

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
        return if (prefs == DietaryPreferences.TEMPLATE_EMPTY) {
            Regex(DietaryPreferences.TEMPLATE_EMPTY)
        } else {
            // Create the variable to insert the regular expression into
            var regexString = ""

            // Create the object to store which preferences need to be met
            val indices = mutableListOf<Int>()

            // Find all preferences that need to be met
            prefs.forEachIndexed { index, c ->
                if (c == DietaryPreferences.C_TRUE) indices.add(index)
            }

            // Needs to be set to correctly assemble the regular expression
            var isFirst = true

            // Iterate through every preference that has been set
            indices.forEach { index ->
                // Set an OR if more than one preference needs to be met
                if (!isFirst) regexString += '|'

                // Add the expression looking for the single preference to the entire string
                regexString += DietaryPreferences.TEMPLATE_EMPTY.substring(0 until index) +
                        DietaryPreferences.C_TRUE +
                        DietaryPreferences.TEMPLATE_EMPTY.substring(index+1)

                // Ensure that OR statements will be placed between the individual expressions
                isFirst = false
            }

            // Compile the string to a regular expression object
            Regex(regexString)
        }
    }

    /**
     * Executed when an item has been clicked.
     *
     * @param offer item that has been clicked
     */
    override fun onClick(offer: CanteenOfferGroupElement, category: String) {
        openBottomSheet(
            AllergenSheet().also { sheet ->
                val bundle = Bundle()
                bundle.putParcelable("offer", offer)
                bundle.putString("category", category)
                bundle.putBoolean("preferenceColour", viewModel.preferenceColour)
                sheet.arguments = bundle
            }
        )
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