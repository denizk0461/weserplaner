package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.databinding.ItemScrollablePageBinding

/**
 * Custom RecyclerView adapter for managing multiple pages of Stud.IP events in a RecyclerView or
 * ViewPager.
 *
 * @param events            all events (not filtered by day at this point)
 * @param onClickListener   for managing click and long press events
 */
class StudIPEventPageAdapter(
    private var events: List<StudIPEvent>,
    private val onClickListener: StudIPEventItemAdapter.OnClickListener,
) : RecyclerView.Adapter<StudIPEventPageAdapter.EventPageViewHolder>() {

    /**
     * View holder class for parent class
     *
     * @param binding   view binding object
     */
    class EventPageViewHolder(val binding: ItemScrollablePageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventPageViewHolder = EventPageViewHolder(
        ItemScrollablePageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    /*
     * Assume that there will always be 5 pages, for 5 days - Monday through Friday.
     * TODO is it right to assume that the user only has events on weekdays?
     */
    override fun getItemCount(): Int = 5

    // Set up page
    override fun onBindViewHolder(holder: EventPageViewHolder, position: Int) {
        holder.binding.pageRecyclerView.apply {
            // Set page to horizontally scroll
            layoutManager = LinearLayoutManager(holder.binding.root.context, LinearLayoutManager.VERTICAL, false)

            /*
             * Create new adapter for every page. Attribute position denotes day that will be set up
             * by the newly created adapter (0 = Monday, 4 = Friday)
             */
            adapter = StudIPEventItemAdapter(events, currentDay = position, onClickListener) // TODO check if empty

            // Animate creation of new page
            scheduleLayoutAnimation()
        }
    }

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