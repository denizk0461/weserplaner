package com.denizk0461.studip.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.R
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.databinding.ItemEventBinding

class StudIPEventAdapter(events: List<StudIPEvent>, private val currentDay: Int) : RecyclerView.Adapter<StudIPEventAdapter.EventViewHolder>() {

    private val filteredEvents: List<StudIPEvent> = events.filter { it.day == currentDay }

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

        if (true) { // highlight
//            holder.binding.cardBackground.cardForegroundColor = holder.binding.root.context.getColorStateList(
//                R.color.list_card_selected)
        } else { // un-highlight
//            holder.binding.cardBackground.cardForegroundColor = Color.TRANSPARENT
        }

        // TODO inflate view saying "no events!" or sth

    }
}