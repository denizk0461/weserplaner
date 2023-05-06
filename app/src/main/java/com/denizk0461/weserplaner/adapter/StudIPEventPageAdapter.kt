package com.denizk0461.weserplaner.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.denizk0461.weserplaner.fragment.EventPageFragment

/**
 * Custom ViewPager adapter for managing multiple pages of Stud.IP events.
 *
 * @param childFragmentManager  child fragment manager of the parent fragment
 * @param lifecycle             parent fragment lifecycle
 */
class StudIPEventPageAdapter(
    childFragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(childFragmentManager, lifecycle) {

    /*
     * Assume that there will always be 7 pages, for 7 days - Monday through Sunday.
     */
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment =
        EventPageFragment().also { f ->
            val bundle = Bundle()
            bundle.putInt("currentDay", position)
            f.arguments = bundle
        }
}