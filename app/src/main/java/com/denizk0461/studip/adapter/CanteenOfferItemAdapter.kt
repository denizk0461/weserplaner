package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.R
import com.denizk0461.studip.databinding.ItemCanteenBinding
import com.denizk0461.studip.databinding.ItemCanteenLineBinding
import com.denizk0461.studip.databinding.ItemIconBinding
import com.denizk0461.studip.model.CanteenOfferGroup
import com.denizk0461.studip.model.CanteenOfferGroupElement

/**
 * Custom RecyclerView adapter that lays out the offers of a given canteen on a given day.
 *
 * @param offers            list of all offers filtered by canteen and day, grouped by category
 * @param onClickListener   used for listening to clicks and long presses
 */
class CanteenOfferItemAdapter(
    private val offers: List<CanteenOfferGroup>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<CanteenOfferItemAdapter.OfferViewHolder>() {

    /**
     * Provides a converting function between the index of a dietary preference and its according
     * icon.
     */
    private val indexToDrawable: Map<Int, Int> = mapOf(
        0 to R.drawable.handshake,
        1 to R.drawable.fish,
        2 to R.drawable.chicken,
        3 to R.drawable.sheep,
        4 to R.drawable.yoga,
        5 to R.drawable.cow,
        6 to R.drawable.pig,
        7 to R.drawable.leaf,
        8 to R.drawable.carrot,
        9 to R.drawable.deer,
        10 to R.drawable.circle,
    )

    /**
     * View holder class for parent class
     *
     * @param binding   view binding object
     */
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

    // Returns the amount of offers for a given canteen and day
    override fun getItemCount(): Int = offers.size

    // TODO inflate a view saying "no events" if none are available for a given day
    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        // Retrieve item for current position
        val currentItem = offers[position]

        // Set category text / header of the item
        holder.binding.textCategory.text = currentItem.category

        // Create a new view (line) for each item displayed within one category
        currentItem.offers.forEach { offer ->
            // Inflate the new line without binding it to the line container
            val line = ItemCanteenLineBinding.inflate(
                LayoutInflater.from(holder.binding.root.context),
            )

            // Set text values
            line.textContent.text = offer.title
            line.textPrice.text = offer.price

            /*
             * Check which dietary preferences are met by this item. This is used to set visual
             * icons on each line. Example: a leaf on vegan items.
             */
            val indices = arrayListOf<Int>()
            offer.dietaryPreferences.filterIndexed { index, char ->
                if (char == 't') {
                    indices.add(index)
                    true
                } else false
            }

            // If no dietary preferences are met, set the view to display a neutral icon
            if (indices.isEmpty()) indices.add(10)

            // Iterate through all icons that need to be displayed
            indices.forEach { index ->
                // Inflate a new icon holder
                val img = ItemIconBinding.inflate(LayoutInflater.from(holder.binding.root.context))

                // Set the appropriate icon
                img.imageView.setImageDrawable(
                    holder.binding.root.context.getDrawable(indexToDrawable[index]!!)
                )

                // Bind the new icon to the line view
                line.imageViewContainer.addView(img.root)
            }

            // Set up single click listener
            line.root.setOnClickListener {
                onClickListener.onClick(offer)
            }

            // Set up long press listener
            line.root.setOnLongClickListener {
                onClickListener.onLongClick(offer)
            }
            // Bind the new line to the line container
            holder.binding.lineContainer.addView(line.root)
        }
    }

    /**
     * Interface used to evaluate clicks and long presses on a given item.
     */
    interface OnClickListener {

        /**
         * Executed when an item has been clicked.
         *
         * @param offer item that has been clicked
         */
        fun onClick(offer: CanteenOfferGroupElement)


        /**
         * Executed when an item has been long-pressed.
         *
         * @param offer item that has been long-pressed
         * @return      whether the long press was successful
         */
        fun onLongClick(offer: CanteenOfferGroupElement): Boolean
    }
}