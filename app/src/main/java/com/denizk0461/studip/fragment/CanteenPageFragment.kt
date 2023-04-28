package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.denizk0461.studip.adapter.CanteenOfferItemAdapter
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.databinding.RecyclerViewBinding
import com.denizk0461.studip.model.CanteenOffer
import com.denizk0461.studip.model.CanteenOfferGroup
import com.denizk0461.studip.model.CanteenOfferGroupElement
import com.denizk0461.studip.model.DietaryPreferences
import com.denizk0461.studip.sheet.AllergenSheet
import com.denizk0461.studip.viewmodel.CanteenPageViewModel

/**
 * Fragment that is instantiated by [StudIPEventPageAdapter] to display individual days' pages and
 * their events.
 *
 * @param currentDay    day to display
 */
class CanteenPageFragment(
    private val currentDay: Int,
) : AppFragment(), CanteenOfferItemAdapter.OnClickListener {

    // Nullable view binding reference
    private var _binding: RecyclerViewBinding? = null

    /*
     * Non-null reference to the view binding. This property is only valid between onCreateView and
     * onDestroyView.
     */
    private val binding get() = _binding!!

    // View model reference for providing access to the database
    private val viewModel: CanteenPageViewModel by viewModels()

    private lateinit var recyclerViewAdapter: CanteenOfferItemAdapter

    private val offerList: MutableList<CanteenOffer> = mutableListOf()

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAdapter = CanteenOfferItemAdapter(
            this,
            viewModel.preferenceAllergen,
        )

        binding.recyclerView.apply {
            // Set page to scroll vertically
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )

            /*
             * Create new adapter for every page. Attribute position denotes day that will be set up
             * by the newly created adapter (0 = Monday, 4 = Friday)
             * TODO check if the filtered list is empty, and tell the user if it is
             */
//            val o = offers.filter { it.dateId == currentDay }

            adapter = recyclerViewAdapter

            // Animate creation of new page
            scheduleLayoutAnimation()

            viewModel.getOffersByDay(currentDay).observe(viewLifecycleOwner) { offers ->
                offerList.clear()
                offerList.addAll(offers)
                recyclerViewAdapter.setNewData(offers.filterElements().groupElements().distinct())
//                filterList()
            }
        }

        viewModel.dietaryPreferencesUpdate.observe(viewLifecycleOwner) {
            filterList()
        }
    }

    private fun filterList() {
        recyclerViewAdapter.setNewData(offerList.filterElements().groupElements().distinct())
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
                    it.category == category && it.date == date && it.canteen == canteen
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

    private fun List<CanteenOffer>.filterElements(): List<CanteenOffer> {

        // Get dietary preference regex
        val prefsRegex = getPrefRegex()

        return if (prefsRegex.toString() == DietaryPreferences.TEMPLATE_EMPTY) {
            // Show all elements and skip filtering if no preference is set
            this
        } else {
            // Filter for dietary preferences
            this.filter {
                prefsRegex.matches(it.dietaryPreferences)
            }
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
        openBottomSheet(AllergenSheet(offer, category))
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