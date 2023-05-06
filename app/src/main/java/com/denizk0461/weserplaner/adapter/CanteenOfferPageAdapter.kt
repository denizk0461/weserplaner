package com.denizk0461.weserplaner.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.denizk0461.weserplaner.fragment.CanteenPageFragment

/**
 * Custom ViewPager adapter for managing multiple pages of canteen offers.
 *
 * @param childFragmentManager  child fragment manager of the parent fragment
 * @param lifecycle             parent fragment lifecycle
 */
class CanteenOfferPageAdapter(
    childFragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(childFragmentManager, lifecycle) {

    private var count: Int = 0

    // Returns the count of days covered
    override fun getItemCount(): Int = count

    override fun createFragment(position: Int): Fragment =
        CanteenPageFragment().also { f ->
            val bundle = Bundle()
            bundle.putInt("currentDay", position)
            f.arguments = bundle
        }

    fun setItemCount(newCount: Int) {
        count = newCount
        notifyDataSetChanged()
    }
}