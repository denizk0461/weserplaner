package com.denizk0461.studip.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.databinding.ItemScrollablePageBinding
import com.denizk0461.studip.model.CanteenOffer

class CanteenOfferPageAdapter(private var offers: List<CanteenOffer>, private var daysCovered: Int, private var prefsRegex: Regex) : RecyclerView.Adapter<CanteenOfferPageAdapter.CanteenOfferPageViewHolder>() {

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
//            Log.d("eek!5", prefsRegex.toString())
            val filteredForDate = offers.filter { it.dateId == position }

            val filteredForPreferences = if (prefsRegex.toString() == "ffffffffff") {
                // show all elements and skip filtering
                filteredForDate
            } else {
                filteredForDate.filter {
                    Log.d("eek!5", it.dietaryPreferences)
                    prefsRegex.matches(it.dietaryPreferences)
                }
            }
//                .filter { it.dietaryPreferences == prefs.deconstruct() }

//            val filtered2 = filtered
//            offers



            adapter = CanteenOfferItemAdapter(filteredForPreferences) // TODO check if empty
            scheduleLayoutAnimation()
        }
    }

    fun setNewItems(items: List<CanteenOffer>, daysCovered: Int) {
        this.daysCovered = daysCovered
        offers = items
        notifyDataSetChanged()
    }

    fun refreshView(prefsRegex: Regex) {
        this.prefsRegex = prefsRegex
        notifyDataSetChanged()
    }

//    private fun constructRegex(pref: String): String =
//        pref.replace('f', '.')
}