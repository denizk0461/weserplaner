package com.denizk0461.studip.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.data.StudIPEvent
import com.denizk0461.studip.databinding.ItemEventBinding
import com.denizk0461.studip.databinding.ItemEventPageBinding

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
//        var itemCount = 0
//        events.filter { it.day == position }.forEach { event ->
//            val newView = ItemEventBinding.inflate(
//                LayoutInflater.from(holder.binding.root.context),
//                holder.binding.root,
//                false
//            ).also { item ->
//                Log.d("HELLO", "generating ${event.title}...2")
//                itemCount += 1
        holder.binding.textTitle.text = currentItem.title
        holder.binding.textLecturers.text = currentItem.lecturer.joinToString(", ")
        holder.binding.textRoom.text = currentItem.room
        holder.binding.textTimeslot.text = currentItem.timeslot
//            }
//            holder.binding.pageLayout.addView(newView.root)
//        }
//        if (itemCount == 0) {
//            // TODO inflate view saying "no events!" or sth
//        }
    }
}