package com.denizk0461.studip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.studip.databinding.ItemCanteenBinding
import com.denizk0461.studip.databinding.ItemCanteenLineBinding
import com.denizk0461.studip.databinding.ItemIconBinding
import com.denizk0461.studip.model.CanteenOfferGroup
import com.denizk0461.studip.model.CanteenOfferGroupElement
import com.denizk0461.studip.model.DietaryPreferences

/**
 * Custom RecyclerView adapter that lays out the offers of a given canteen on a given day.
 *
 * @param offers            list of all offers filtered by canteen and day, grouped by category
 * @param onClickListener   used for listening to clicks and long presses
 * @param displayAllergens  whether the user wants allergens to be marked
 */
class CanteenOfferItemAdapter(
    private val offers: List<CanteenOfferGroup>,
    private val onClickListener: OnClickListener,
    private val displayAllergens: Boolean,
) : RecyclerView.Adapter<CanteenOfferItemAdapter.OfferViewHolder>() {

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
            line.textContent.text = offer.title.addAllergenNotice(displayAllergens, offer.allergens)
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
                    AppCompatResources.getDrawable(
                        holder.binding.root.context,
                        DietaryPreferences.indexToDrawable[index]!!,
                    )
                )

                // Bind the new icon to the line view
                line.imageViewContainer.addView(img.root)
            }

            // Set up single click listener
            line.root.setOnClickListener {
                onClickListener.onClick(offer, currentItem.category)
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
     * Adds a star to mark allergens present in an offer. Allergens will be displayed as a star '*'
     * next to the title.
     *
     * @param displayAllergens  whether the user wants allergens to be displayed
     * @param allergens         allergens present in the offer
     * @return                  the formatted string
     */
    private fun String.addAllergenNotice(displayAllergens: Boolean, allergens: String): String {

        /*
         * Don't add anything to the string if the user does not want a star added, or if the item
         * does not contain any allergens or additives.
         */
        if (!displayAllergens || allergens.isBlank()) return this

        // Add a star to the string
        return "$this*"
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
        fun onClick(offer: CanteenOfferGroupElement, category: String)


        /**
         * Executed when an item has been long-pressed.
         *
         * @param offer item that has been long-pressed
         * @return      whether the long press was successful
         */
        fun onLongClick(offer: CanteenOfferGroupElement): Boolean
    }
}