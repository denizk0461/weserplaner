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
import com.denizk0461.weserplaner.values.DietaryPreferences
import com.denizk0461.weserplaner.sheet.AllergenSheet
import com.denizk0461.weserplaner.viewmodel.CanteenPageViewModel

/**
 * Fragment that is instantiated by [StudIPEventPageAdapter] to display individual days' pages and
 * their events.
 */
class CanteenPageFragment : AppFragment<RecyclerViewBinding>(), CanteenOfferItemAdapter.OnClickListener {

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
                recyclerViewAdapter.setData(
                    offers,
                    viewModel.getDietaryPrefs().deconstruct(),
                    viewModel.preferenceAllergenConfig,
                )
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
                val differenceY = scrollY - oldScrollY
                if (differenceY > 0) {
                    // If scrolling down, shrink the FAB
                    parentFragment.shrinkFab()
                } else if (differenceY < 0) {
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
            recyclerViewAdapter.setData(offerList,
                viewModel.getDietaryPrefs().deconstruct(),
                viewModel.preferenceAllergenConfig,
            )
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