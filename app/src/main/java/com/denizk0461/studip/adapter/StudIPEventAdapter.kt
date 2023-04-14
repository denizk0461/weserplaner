package com.denizk0461.studip.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.R
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.databinding.ItemEventBinding
import java.util.*

class StudIPEventAdapter(
    events: List<StudIPEvent>, private val currentDay: Int, private val onClickListener: OnClickListener
) : RecyclerView.Adapter<StudIPEventAdapter.EventViewHolder>() {

    private val filteredEvents: List<StudIPEvent> = events.filter { it.day == currentDay }
    private val currentCalendar = Calendar.getInstance()
    private var isAnyCourseHighlighted = false

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

    override fun getItemCount(): Int = filteredEvents.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = filteredEvents[position]

        holder.binding.textTitle.text = currentItem.title
        holder.binding.textLecturers.text = currentItem.lecturer
        holder.binding.textRoom.text = currentItem.room
        holder.binding.textTimeslot.text = currentItem.timeslot()

        val colorPrimary = TypedValue()
        val colorTextHint = TypedValue()
        val theme = holder.binding.root.context.theme
        theme.resolveAttribute(R.attr.colorPrimary, colorPrimary, true)
        theme.resolveAttribute(R.attr.colorTextHintLighter, colorTextHint, true)

        if (!isAnyCourseHighlighted && currentItem.isCurrentCourse(currentCalendar)) { // highlight
            isAnyCourseHighlighted = true
            holder.binding.cardBackground.strokeColor = colorPrimary.data
            holder.binding.cardBackground.strokeWidth = 6
//                R.color.list_card_selected)
        } else { // un-highlight
            holder.binding.cardBackground.strokeColor = colorTextHint.data
            holder.binding.cardBackground.strokeWidth = 3
        }

        holder.binding.cardBackground.setOnClickListener {
            onClickListener.onClick(currentItem)
        }
        holder.binding.cardBackground.setOnLongClickListener {
            onClickListener.onLongClick(currentItem)
            true
        }

        // TODO inflate view saying "no events!" or sth

    }

    private fun StudIPEvent.isCurrentCourse(calendar: Calendar): Boolean =
        (calendar.get(Calendar.DAY_OF_WEEK) == this.day.toCalendarDay()) &&
                ((calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60) < this.timeslotEnd.parseToMinutes())

    // Converts day as defined in StudIPEvent.kt to day from Calendar
    private fun Int.toCalendarDay(): Int = when (this) {
        1 -> Calendar.TUESDAY
        2 -> Calendar.WEDNESDAY
        3 -> Calendar.THURSDAY
        4 -> Calendar.FRIDAY
        5 -> Calendar.SATURDAY
        6 -> Calendar.SUNDAY
        else -> Calendar.MONDAY
    }

    private fun String.parseToMinutes(): Int {
        val parts = split(":")
        return (parts[0].toInt() * 60) + parts[1].toInt()
    }

    interface OnClickListener {
        fun onClick(event: StudIPEvent)
        fun onLongClick(event: StudIPEvent)
    }
}