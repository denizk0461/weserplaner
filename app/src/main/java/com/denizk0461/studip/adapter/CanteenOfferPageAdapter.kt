package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.databinding.ItemScrollablePageBinding
import com.denizk0461.studip.model.CanteenOfferGroup

class CanteenOfferPageAdapter(private var offers: List<CanteenOfferGroup>, private var daysCovered: Int, private var prefsRegex: Regex) : RecyclerView.Adapter<CanteenOfferPageAdapter.CanteenOfferPageViewHolder>() {

    class CanteenOfferPageViewHolder(val binding: ItemScrollablePageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CanteenOfferPageViewHolder =
        CanteenOfferPageViewHolder(
            ItemScrollablePageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
    )

    override fun getItemCount(): Int = daysCovered

    override fun onBindViewHolder(holder: CanteenOfferPageViewHolder, position: Int) {

        holder.binding.pageRecyclerView.apply {

            layoutManager = LinearLayoutManager(holder.binding.root.context, LinearLayoutManager.VERTICAL, false)

            adapter = CanteenOfferItemAdapter(offers.filter { it.dateId == position }) // TODO check if empty
            scheduleLayoutAnimation()
        }
    }

    fun setNewItems(items: List<CanteenOfferGroup>, daysCovered: Int) {
        this.daysCovered = daysCovered
        offers = items
        notifyDataSetChanged()
    }
}