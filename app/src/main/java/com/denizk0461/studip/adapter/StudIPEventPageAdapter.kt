package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.model.StudIPEvent
import com.denizk0461.studip.databinding.ItemEventPageBinding

class StudIPEventPageAdapter(private var events: List<StudIPEvent>) : RecyclerView.Adapter<StudIPEventPageAdapter.EventPageViewHolder>() {

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
            adapter = StudIPEventAdapter(events, currentDay = position) // TODO check if empty
            scheduleLayoutAnimation()
        }
    }

    fun setNewItems(items: List<StudIPEvent>) {
        events = items
        notifyDataSetChanged()
    }
}