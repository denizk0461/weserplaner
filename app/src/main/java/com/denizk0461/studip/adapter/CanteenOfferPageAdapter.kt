package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.databinding.ItemScrollablePageBinding
import com.denizk0461.studip.model.CanteenOfferGroup

/**
 * Custom RecyclerView adapter for managing multiple pages of canteen offers in a RecyclerView or
 * ViewPager.
 *
 * @param offers            all offers for a given canteen, grouped by category (not filtered by day at
 *                          this point)
 * @param daysCovered       tells for how many days offers are available for.
 *                          Example: if the next two weeks are available, Monday through Friday and
 *                          excluding weekends, this value should be 10.
 * @param onClickListener   for managing click and long press events
 * @param displayAllergens  whether the user wants allergens to be marked
 */
class CanteenOfferPageAdapter(
    private var offers: List<CanteenOfferGroup>,
    private var daysCovered: Int,
    private val onClickListener: CanteenOfferItemAdapter.OnClickListener,
    private val displayAllergens: Boolean,
) : RecyclerView.Adapter<CanteenOfferPageAdapter.CanteenOfferPageViewHolder>() {

    /**
     * View holder class for parent class
     *
     * @param binding   view binding object
     */
    class CanteenOfferPageViewHolder(val binding: ItemScrollablePageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CanteenOfferPageViewHolder =
        CanteenOfferPageViewHolder(
            ItemScrollablePageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
    )

    // Returns the count of days covered
    override fun getItemCount(): Int = daysCovered

    override fun onBindViewHolder(holder: CanteenOfferPageViewHolder, position: Int) {

        holder.binding.pageRecyclerView.apply {
            // Set page to horizontally scroll
            layoutManager = LinearLayoutManager(
                holder.binding.root.context, LinearLayoutManager.VERTICAL, false
            )

            /*
             * Create new adapter for every page. Attribute position denotes day that will be set up
             * by the newly created adapter (0 = Monday, 4 = Friday)
             * TODO check if the filtered list is empty, and tell the user if it is
             */
            adapter = CanteenOfferItemAdapter(
                offers.filter { it.dateId == position },
                onClickListener,
                displayAllergens,
            )

            // Animate creation of new page
            scheduleLayoutAnimation()
        }
    }

    /**
     * Update the entire list of items
     *
     * @param items the new set of items
     */
    fun setNewItems(items: List<CanteenOfferGroup>, daysCovered: Int) {
        // Update the count of days covered
        this.daysCovered = daysCovered

        // Update items
        offers = items

        // Force an update of the view. TODO replace with individual updates, as this is inefficient
        notifyDataSetChanged()
    }
}