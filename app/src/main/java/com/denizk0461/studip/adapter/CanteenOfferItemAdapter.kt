package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.databinding.ItemCanteenBinding
import com.denizk0461.studip.model.CanteenOffer

class CanteenOfferItemAdapter(private val offers: List<CanteenOffer>) : RecyclerView.Adapter<CanteenOfferItemAdapter.OfferViewHolder>() {

    class OfferViewHolder(val binding: ItemCanteenBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        return OfferViewHolder(
            ItemCanteenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = offers.size

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val currentItem = offers[position]

        holder.binding.textCategory.text = currentItem.category
        holder.binding.tempContent.text = "${currentItem.title} • ${currentItem.price}"

        // TODO inflate view saying "no offers!" or sth
    }

//    interface OnClickListener {
//        fun onClick(event: StudIPEvent)
//        fun onLongClick(event: StudIPEvent)
//    }
}