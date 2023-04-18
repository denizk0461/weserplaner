package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.databinding.ItemScrollablePageBinding
import com.denizk0461.studip.model.CanteenOffer
import com.denizk0461.studip.model.DietaryPrefObject

class CanteenOfferPageAdapter(private var offers: List<CanteenOffer>, private var daysCovered: Int, private var prefs: DietaryPrefObject) : RecyclerView.Adapter<CanteenOfferPageAdapter.CanteenOfferPageViewHolder>() {

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

            // TODO second filter needs to be worked on; it shouldn't filter out something if two options are selected and only one is met
            val filtered = offers.filter { it.dateId == position }.filter { it.dietaryPreferences == prefs.deconstruct() }
            adapter = CanteenOfferItemAdapter(filtered) // TODO check if empty
            scheduleLayoutAnimation()
        }
    }

    fun setNewItems(items: List<CanteenOffer>, daysCovered: Int) {
        this.daysCovered = daysCovered
        offers = items
        notifyDataSetChanged()
    }

    fun refreshView(prefs: DietaryPrefObject) {
        this.prefs = prefs
        notifyDataSetChanged()
    }
}