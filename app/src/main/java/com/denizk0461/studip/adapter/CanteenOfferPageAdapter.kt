package com.denizk0461.studip.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.denizk0461.studip.fragment.CanteenPageFragment


/**
 * Custom ViewPager adapter for managing multiple pages of canteen offers.
 *
 * @param fragmentActivity  parent fragment activity
 */
class CanteenOfferPageAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {

    private var count: Int = 0

    // Returns the count of days covered
    override fun getItemCount(): Int = count

    override fun createFragment(position: Int): Fragment =
        CanteenPageFragment(
            position,
        )

    fun setItemCount(newCount: Int) {
        count = newCount
        notifyDataSetChanged()
    }
}