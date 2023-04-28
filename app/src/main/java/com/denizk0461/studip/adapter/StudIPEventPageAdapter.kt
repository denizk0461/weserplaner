package com.denizk0461.studip.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.denizk0461.studip.fragment.EventPageFragment

/**
 * Custom ViewPager adapter for managing multiple pages of Stud.IP events.
 *
 * @param fragmentActivity  parent fragment activity
 */
class StudIPEventPageAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {

    /*
     * Assume that there will always be 7 pages, for 7 days - Monday through Sunday.
     */
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment =
        EventPageFragment(position)
}