package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.denizk0461.studip.adapter.CanteenOfferItemAdapter
import com.denizk0461.studip.adapter.StudIPEventPageAdapter
import com.denizk0461.studip.databinding.RecyclerViewBinding
import com.denizk0461.studip.model.CanteenOfferGroup

/**
 * Fragment that is instantiated by [StudIPEventPageAdapter] to display individual days' pages and
 * their events.
 *
 * @param offers            all offers for a given canteen, grouped by category (not filtered by day at
 *                          this point)
 * @param onClickListener   for managing click and long press events
 * @param displayAllergens  whether the user wants allergens to be marked
 * @param currentDay        current day
 */
class CanteenPageFragment(
    private var offers: List<CanteenOfferGroup>,
    private val onClickListener: CanteenOfferItemAdapter.OnClickListener,
    private val displayAllergens: Boolean,
    private val currentDay: Int,
) : AppFragment() {

    // Nullable view binding reference
    private var _binding: RecyclerViewBinding? = null

    /*
     * Non-null reference to the view binding. This property is only valid between onCreateView and
     * onDestroyView.
     */
    private val binding get() = _binding!!

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val o = offers.filter { it.dateId == currentDay }

            adapter = CanteenOfferItemAdapter(
                o,
                onClickListener,
                displayAllergens,
            )

            // Animate creation of new page
            scheduleLayoutAnimation()
        }
    }
}