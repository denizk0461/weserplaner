package com.denizk0461.studip.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.denizk0461.studip.fragment.CanteenPageFragment
import com.denizk0461.studip.model.CanteenOfferGroup


/**
 * Custom ViewPager adapter for managing multiple pages of canteen offers.
 *
 * @param fragmentActivity  parent fragment activity
 * @param offers            all offers for a given canteen, grouped by category (not filtered by day at
 *                          this point)
 * @param daysCovered       tells for how many days offers are available for.
 *                          Example: if the next two weeks are available, Monday through Friday and
 *                          excluding weekends, this value should be 10.
 * @param onClickListener   for managing click and long press events
 * @param displayAllergens  whether the user wants allergens to be marked
 */
class CanteenOfferPageAdapter(
    fragmentActivity: FragmentActivity,
    private var offers: List<CanteenOfferGroup>,
    private var daysCovered: Int,
    private val onClickListener: CanteenOfferItemAdapter.OnClickListener,
    private val displayAllergens: Boolean,
) : FragmentStateAdapter(fragmentActivity) {

    // Returns the count of days covered
    override fun getItemCount(): Int = daysCovered

    override fun createFragment(position: Int): Fragment =
        CanteenPageFragment(
            offers,
            onClickListener,
            displayAllergens,
            position,
        )

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