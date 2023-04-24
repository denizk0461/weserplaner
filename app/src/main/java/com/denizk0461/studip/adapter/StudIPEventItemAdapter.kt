package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.data.parseToMinutes
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.databinding.ItemEventBinding
import java.util.*

/**
 * Custom RecyclerView adapter that lays out the Stud.IP events of a given day.
 *
 * @param events            list of all events (still not filtered by day at this point)
 * @param currentDay        current day that is used to show only events of a given day (0 = Monday,
 *                          4 = Friday)
 * @param onClickListener   used for listening to clicks and long presses
 */
class StudIPEventItemAdapter(
    events: List<StudIPEvent>,
    private val currentDay: Int,
    private val onClickListener: OnClickListener,
) : RecyclerView.Adapter<StudIPEventItemAdapter.EventViewHolder>() {

    // Filter events to only show those of a given day
    private val filteredEvents: List<StudIPEvent> = events.filter { it.day == currentDay }

    /*
     * Retrieve an instance of Calendar to check whether the adapter's day matches the current day.
     * TODO this can be optimised by checking in StudIPEventPageAdapter.kt and delivering a boolean
     */
    private val currentCalendar = Calendar.getInstance()

    /*
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
    override fun getItemCount(): Int = filteredEvents.size

    // TODO inflate a view saying "no events" if none are available for a given day
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // Retrieve item for current position
        val currentItem = filteredEvents[position]

        // Set up text fields with corresponding values
        holder.binding.textTitle.text = currentItem.title
        holder.binding.textLecturers.text = currentItem.lecturer
        holder.binding.textRoom.text = currentItem.room

        // Use parsed timeslot string as defined in StudIPEvent#timeslot()
        holder.binding.textTimeslot.text = currentItem.timeslot()

        // TODO highlighting functionality is currently not supported!
        // Set up colours used for highlighting and un-highlighting an item.
//        val colorPrimaryTranslucent = TypedValue()
//        val colorCardBackground = TypedValue()
//        val colorTextHintLighter = TypedValue()

        // Resolve themed attributes to get the right values for day and night modes
//        holder.binding.root.context.theme.apply {
//            resolveAttribute(com.google.android.material.R.attr.colorSecondaryContainer, colorPrimaryTranslucent, true)
//            resolveAttribute(R.attr.colorCardBackground, colorCardBackground, true)
//            resolveAttribute(R.attr.colorTextHintLighter, colorTextHintLighter, true)
//        }

        /* Highlight the next upcoming course of the day if the day of the adapter matches the
         * current day, and if no other course has been highlighted, to avoid double highlighting.
         */
//        if (!isAnyCourseHighlighted && currentItem.isCurrentCourse(currentCalendar)) {
//            // Ensure that no other course will be highlighted
//            isAnyCourseHighlighted = true
//
//            holder.binding.cardBackground.apply {
//                // Set the card's background colour to a desaturated shade of the primary colour
//                backgroundTintList = ColorStateList.valueOf(colorPrimaryTranslucent.data)
//
//                // Hide card stroke
//                strokeColor = context.getColor(android.R.color.transparent)
//            }
//        // Otherwise, apply colours to ensure that the item will not be highlighted
//        } else {
//            holder.binding.cardBackground.apply {
//                // Set the card's background colour to its default value
//                backgroundTintList = ColorStateList.valueOf(colorCardBackground.data)
//
//                // Set the card's stroke to its default colour
//                strokeColor = colorTextHintLighter.data
//            }
//        }

        // Set up single click listener
        holder.binding.cardBackground.setOnClickListener {
            onClickListener.onClick(currentItem)
        }

        // Set up long press listener
        holder.binding.cardBackground.setOnLongClickListener {
            onClickListener.onLongClick(currentItem)
        }
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
                ((calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60) < this.timeslotEnd.parseToMinutes())

    // Converts day as defined in StudIPEvent.kt to day from Calendar
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