package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.data.Misc
import com.denizk0461.studip.databinding.ItemCanteenBinding
import com.denizk0461.studip.databinding.ItemCanteenLineBinding
import com.denizk0461.studip.databinding.ItemIconBinding
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

        val line = ItemCanteenLineBinding.inflate(
            LayoutInflater.from(holder.binding.root.context),
//            holder.binding.root,
//            true,
        )
        holder.binding.textCategory.text = currentItem.category
        line.content.text = currentItem.title
        val indices = arrayListOf<Int>()
        currentItem.dietaryPreferences.filterIndexed { index, char ->
            if (char == 't') {
                indices.add(index)
                true
            } else false
        }
        if (indices.isEmpty()) indices.add(10)
        indices.forEach { index ->
            val img = ItemIconBinding.inflate(LayoutInflater.from(holder.binding.root.context))
//            holder.binding.root.resources.getDrawable(Misc.indexToDrawable[index]!!, holder.binding.root.context.theme)
            img.imageView.setImageDrawable(holder.binding.root.context.getDrawable(Misc.indexToDrawable[index]!!))
            line.imageViewContainer.addView(img.root)
        }

        holder.binding.lineContainer.addView(line.root)

//        holder.binding.textCategory.text = currentItem.category
//        holder.binding.tempContent.text = "${currentItem.title} • ${currentItem.price}"

        // TODO inflate view saying "no offers!" or sth
    }

//    interface OnClickListener {
//        fun onClick(event: StudIPEvent)
//        fun onLongClick(event: StudIPEvent)
//    }
}