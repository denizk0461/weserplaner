package com.denizk0461.studip.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.data.StudIPEvent
import com.denizk0461.studip.databinding.ItemEventBinding
import com.denizk0461.studip.databinding.ItemEventPageBinding

class StudIPEventPageAdapter(private val events: List<StudIPEvent>) : RecyclerView.Adapter<StudIPEventPageAdapter.EventPageViewHolder>() {

    class EventPageViewHolder(val binding: ItemEventPageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventPageViewHolder = EventPageViewHolder(
        ItemEventPageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: EventPageViewHolder, position: Int) {

        holder.binding.pageRecyclerView.apply {
            layoutManager = LinearLayoutManager(holder.binding.root.context, LinearLayoutManager.VERTICAL, false)
            adapter = StudIPEventAdapter(events.filter { it.day == position }) // TODO check if empty
        }

//        var itemCount = 0
//        events.filter { it.day == position }.forEach { event ->
//            val newView = ItemEventBinding.inflate(
//                LayoutInflater.from(holder.binding.root.context),
//                holder.binding.root,
//                false
//            ).also { item ->
//                Log.d("HELLO", "generating ${event.title}...2")
//                itemCount += 1
//                item.textTitle.text = event.title
//                item.textLecturers.text = event.lecturer.joinToString(", ")
//                item.textRoom.text = event.room
//                item.textTimeslot.text = event.timeslot
//            }
//            holder.binding.pageLayout.addView(newView.root)
//        }
//        if (itemCount == 0) {
//            // TODO inflate view saying "no events!" or sth
//        }
    }
}