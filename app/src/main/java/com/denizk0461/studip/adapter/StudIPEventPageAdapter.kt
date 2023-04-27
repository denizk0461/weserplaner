package com.denizk0461.studip.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.fragment.EventPageFragment

/**
 * Custom ViewPager adapter for managing multiple pages of Stud.IP events.
 *
 * @param fragmentActivity  parent fragment activity
 * @param events            all events (not filtered by day at this point)
 * @param onClickListener   for managing click and long press events
 */
class StudIPEventPageAdapter(
    fragmentActivity: FragmentActivity,
    private var events: List<StudIPEvent>,
    private val onClickListener: StudIPEventItemAdapter.OnClickListener,
) : FragmentStateAdapter(fragmentActivity) {

    /*
     * Assume that there will always be 7 pages, for 7 days - Monday through Sunday.
     */
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment =
        EventPageFragment(events, position, onClickListener)

    /**
     * Update the entire list of items
     *
     * @param items the new set of items
     */
    fun setNewItems(items: List<StudIPEvent>) {
        // Update items
        events = items

        // Force an update of the view. TODO replace with individual updates, as this is inefficient
        notifyDataSetChanged()
    }
}