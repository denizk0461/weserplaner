package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.databinding.ItemEventBinding

class StudIPEventAdapter(private val events: List<StudIPEvent>) : RecyclerView.Adapter<StudIPEventAdapter.EventViewHolder>() {

    class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder = EventViewHolder(
        ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = events[position]

        holder.binding.textTitle.text = currentItem.title
        holder.binding.textLecturers.text = currentItem.lecturer
        holder.binding.textRoom.text = currentItem.room
        holder.binding.textTimeslot.text = currentItem.timeslot()

        // TODO inflate view saying "no events!" or sth

    }
}