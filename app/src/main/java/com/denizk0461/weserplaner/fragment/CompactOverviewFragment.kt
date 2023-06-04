package com.denizk0461.weserplaner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.adapter.CanteenOfferItemAdapter
import com.denizk0461.weserplaner.data.setRainbowProgressCircle
import com.denizk0461.weserplaner.data.showErrorSnackBar
import com.denizk0461.weserplaner.databinding.FragmentCompactOverviewBinding
import com.denizk0461.weserplaner.model.CanteenOfferGroupElement
import com.denizk0461.weserplaner.sheet.AllergenSheet
import com.denizk0461.weserplaner.viewmodel.CompactOverviewViewModel

class CompactOverviewFragment : AppFragment<FragmentCompactOverviewBinding>(),
    CanteenOfferItemAdapter.OnClickListener {

    // View model
    private val viewModel: CompactOverviewViewModel by viewModels()

    // Recycler view adapter for displaying the most recent day's offers
    private lateinit var offerAdapter: CanteenOfferItemAdapter

    // Whether news for the selected canteen are available
    private var areNewsAvailable = false

    // Date for which offers will be shown
    private var offerDate = ""

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCompactOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set progress circle colours
        binding.swipeRefreshLayout.setRainbowProgressCircle()

        // Set up on click listener for the settings button
        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_compact_overview_to_settings)
        }

        // Set up click listener for offer field
        binding.fieldTimetable.setOnClickListener {
            viewTimetable()
        }

        // Set up click listener for offer button
        binding.buttonTimetable.setOnClickListener {
            viewTimetable()
        }

        binding.eventRecyclerView.apply {

        }

        // Set header text for the offers
        binding.textOfferHeader.text = getString(
            R.string.compact_overview_header_offers,
            getCurrentlySelectedCanteenName(),
        )

        viewModel.getCanteen().observe(viewLifecycleOwner) { canteen ->
            // Decide and save whether news are available
            areNewsAvailable = !(canteen == null || canteen.news.isBlank())

            // Refresh offer subheader text
            binding.textOfferSubheader.refreshOfferSubheader()
        }

        // Set offer headers to be selected so it scrolls (marquee)
        binding.textOfferHeader.isSelected = true
        binding.textOfferSubheader.isSelected = true

        // Set up click listener for offer field
        binding.fieldAllOffers.setOnClickListener {
            viewAllOffers()
        }

        // Set up click listener for offer button
        binding.buttonAllOffers.setOnClickListener {
            viewAllOffers()
        }

        // Set up refresh button
        binding.buttonRefreshOffers.setOnClickListener {
            refreshOffers()
        }

        // Set the refresh layout to disabled to disallow pull-to-refresh
        binding.swipeRefreshLayout.isEnabled = false

        // Set up offer adapter
        offerAdapter = CanteenOfferItemAdapter(
            this,
            viewModel.preferenceAllergen,
            viewModel.preferenceColour,
        )

        // Set up offer recycler view
        binding.offerRecyclerView.apply {
            // Set page to scroll vertically
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )

            adapter = offerAdapter

            // Animate creation of new page
            scheduleLayoutAnimation()

            // Observe offers and re-set them if they change
            viewModel.getOffersByDay(0).observe(viewLifecycleOwner) { offers ->

                offerDate = if (offers.isEmpty()) {
                    ""
                } else {
                    offers[0].date
                }

                // Refresh offer subheader text
                binding.textOfferSubheader.refreshOfferSubheader()

                // Set new data
                offerAdapter.setData(
                    offers,
                    viewModel.getDietaryPrefs().deconstruct(),
                    viewModel.preferenceAllergenConfig,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Set header text for the offers in case the user has switched canteens
        binding.textOfferHeader.text = getString(
            R.string.compact_overview_header_offers,
            getCurrentlySelectedCanteenName(),
        )
    }

    /**
     * Sets the appropriate text for the offer subheader.
     */
    private fun AppCompatTextView.refreshOfferSubheader() {
        // Set specific text if no offers have been found or fetched
        if (offerDate.isBlank()) {
            text = getString(R.string.compact_overview_subheader_offers_empty)
            return
        }

        // Set text depending on whether news are available
        text = getString(if (areNewsAvailable) {
            R.string.compact_overview_subheader_offers_news
        } else {
            R.string.compact_overview_subheader_offers_no_news
        }, offerDate)
        return
    }

    /**
     * Navigates to [EventFragment].
     */
    private fun viewTimetable() {
        findNavController().navigate(R.id.action_compact_overview_to_schedule)
    }

    /**
     * Navigates to [CanteenFragment].
     */
    private fun viewAllOffers() {
        findNavController().navigate(R.id.action_compact_overview_to_canteen)
    }

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
    private fun refreshOffers() {
        // Tell the swipe refresh layout to refresh
        binding.swipeRefreshLayout.isRefreshing = true

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
                    binding.rootView,
                    getString(R.string.canteen_fetch_error)
                )
                /*
                 * Tell the swipe refresh layout to stop refreshing.
                 */
                binding.swipeRefreshLayout.isRefreshing = false
            }
        })
    }

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

    override fun onLongClick(offer: CanteenOfferGroupElement): Boolean {
        return false
    }
}