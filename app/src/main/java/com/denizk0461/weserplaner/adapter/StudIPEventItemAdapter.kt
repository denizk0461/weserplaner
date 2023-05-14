package com.denizk0461.weserplaner.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.weserplaner.data.AppDiffUtilCallback
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.getThemedColor
import com.denizk0461.weserplaner.data.parseToMinutes
import com.denizk0461.weserplaner.model.StudIPEvent
import com.denizk0461.weserplaner.databinding.ItemEventBinding
import java.util.*

/**
 * Custom RecyclerView adapter that lays out the Stud.IP events for a given day.
 *

 * @param currentDay        current day that is used to show only a given day's events (0 = Monday,
 *                          4 = Friday, 6 = Sunday)
 * @param onClickListener   used for listening to clicks and long presses
 */
class StudIPEventItemAdapter(
    private val currentDay: Int,
    private val highlightNextCourse: Boolean,
    private val onClickListener: OnClickListener,
) : RecyclerView.Adapter<StudIPEventItemAdapter.EventViewHolder>() {

    /**
     * List of all events.
     */
    private val events: MutableList<StudIPEvent> = mutableListOf()

    /**
     * Retrieve an instance of Calendar to check whether the adapter's day matches the current day.
     */
    private val currentCalendar = Calendar.getInstance()

    /**
     * Used to disallow highlighting more than one course at a time, since only a single course
     * could be coming up at a given time.
     */
    private var isAnyCourseHighlighted = false

    /**
     * View holder class for parent class
     *
     * @param binding   view binding object
     */
    class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // Set the amount of items to the size of the filtered list, so only events of the current day
    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {

        // Retrieve item for current position
        val currentItem = events[position]

        // Context for easier reference
        val context = holder.binding.root.context

        // Display a notice to the user saying they have no events scheduled for the day
        if (currentItem.eventId == -1) {
            holder.binding.cardBackground.visibility = View.GONE
            holder.binding.noEventsContainer.visibility = View.VISIBLE
            holder.binding.textNoEvents.text = context.getString(
                R.string.schedule_no_events,
                context.resources.getStringArray(R.array.weekdays)[currentDay]
            )
            return
        }

        // Set visibility of the elements accordingly if there are events scheduled
        holder.binding.cardBackground.visibility = View.VISIBLE
        holder.binding.noEventsContainer.visibility = View.GONE

        // Set up text fields with corresponding values
        holder.binding.textTitle.text = currentItem.title
        holder.binding.textLecturers.text = currentItem.lecturer
        holder.binding.textRoom.text = currentItem.room

        // Use parsed timeslot string as defined in StudIPEvent#timeslot()
        holder.binding.textTimeslot.text = currentItem.timeslot()

        // Get current theme
        val theme = context.theme

        /*
         * Highlight the next upcoming course of the day if the user selected the option, if the day
         * of the adapter matches the current day, and if no other course has been highlighted, to
         * avoid double highlighting.
         */
        if (highlightNextCourse &&
            !isAnyCourseHighlighted &&
            currentItem.isCurrentCourse(currentCalendar)
        ) {
            // Ensure that no other course will be highlighted
            isAnyCourseHighlighted = true

            holder.binding.cardBackground.apply {
                // Set the card's background colour to a desaturated shade of the primary colour
                backgroundTintList =
                    ColorStateList.valueOf(theme.getThemedColor(R.attr.colorSecondaryContainer))

                // Hide card stroke
                strokeColor = context.getColor(android.R.color.transparent)
            }
            // Set the divider colour to the text colour
            holder.binding.divider.dividerColor =
                theme.getThemedColor(R.attr.colorText)

            // Set ripple colour
            holder.binding.linearLayout.background = getDrawable(
                holder.binding.root.context,
                R.drawable.selectable_item_background_highlighted,
            )

        // Otherwise, apply colours to ensure that the item will not be highlighted
        } else {
            holder.binding.cardBackground.apply {
                // Set the card's background colour to its default value
                backgroundTintList =
                    ColorStateList.valueOf(theme.getThemedColor(
                        com.google.android.material.R.attr.colorSurface
                    ))

                // Set the card's stroke to its default colour
                strokeColor = theme.getThemedColor(R.attr.colorOutlineVariant)
            }
            // Set the divider colour to its default value
            holder.binding.divider.dividerColor = theme.getThemedColor(R.attr.colorOutlineVariant)

            // Set ripple colour
            holder.binding.linearLayout.background = getDrawable(
                holder.binding.root.context,
                R.drawable.selectable_item_background,
            )
        }

        // Set up single click listener
        holder.binding.linearLayout.setOnClickListener {
            onClickListener.onClick(currentItem)
        }

        // Set up long press listener
        holder.binding.linearLayout.setOnLongClickListener {
            onClickListener.onLongClick(currentItem)
        }
    }

    /**
     * Updates the data and calculates the difference between the old dataset and the newly provided
     * dataset.
     *
     * @param newData   new dataset to be displayed
     */
    fun setNewData(newData: List<StudIPEvent>) {
        // Calculate the difference between the old list and the new list
        val diffResult = DiffUtil.calculateDiff(AppDiffUtilCallback(events, newData))

        // Remove all items from the list
        events.clear()

        // Add all items from the new list
        events.addAll(newData)

        // Tell the DiffUtil which items have changed between the two lists
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * Checks if a given course is the next course coming up. This is done by checking whether the
     * day of the course matches the current real world day, as well as by evaluating if the end
     * time stamp of the event has already passed. As the course list is ordered chronologically,
     * this should always find the 'next' event, since without ordering the data origin, this could
     * be used to highlight any event of the day that hasn't already passed.
     *
     * @param calendar  instance of the current calendar
     * @return          whether the course is coming up next
     */
    private fun StudIPEvent.isCurrentCourse(calendar: Calendar): Boolean =
        (calendar.get(Calendar.DAY_OF_WEEK) == this.day.toCalendarDay()) &&
                ((calendar.get(Calendar.MINUTE) + calendar.get(
                    Calendar.HOUR_OF_DAY
                ) * 60) < this.timeslotEnd.parseToMinutes())

    /**
     * Converts a numeric value as defined in StudIPEvent.kt to a calendar day. Used to convert from
     * American convention (where Sunday is the first day, 1 = Sunday, 2 = Monday, 7 = Saturday) to
     * European convention and zero-based indexing (0 = Monday, 6 = Sunday).
     *
     * @return  calendar day as numeric value
     */
    private fun Int.toCalendarDay(): Int = when (this) {
        1 -> Calendar.TUESDAY
        2 -> Calendar.WEDNESDAY
        3 -> Calendar.THURSDAY
        4 -> Calendar.FRIDAY
        5 -> Calendar.SATURDAY
        6 -> Calendar.SUNDAY
        else -> Calendar.MONDAY
    }

    /**
     * Interface used to evaluate clicks and long presses on a given item.
     */
    interface OnClickListener {

        /**
         * Executed when an item has been clicked.
         *
         * @param event item that has been clicked
         */
        fun onClick(event: StudIPEvent)


        /**
         * Executed when an item has been long-pressed.
         *
         * @param event item that has been long-pressed
         * @return      whether the long press was successful
         */
        fun onLongClick(event: StudIPEvent): Boolean
    }
}