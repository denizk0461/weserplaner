package com.denizk0461.weserplaner.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.AppDiffUtilCallback
import com.denizk0461.weserplaner.data.getThemedColor
import com.denizk0461.weserplaner.databinding.ItemCanteenBinding
import com.denizk0461.weserplaner.databinding.ItemCanteenLineBinding
import com.denizk0461.weserplaner.databinding.ItemIconBinding
import com.denizk0461.weserplaner.model.CanteenOfferGroup
import com.denizk0461.weserplaner.model.CanteenOfferGroupElement
import com.denizk0461.weserplaner.model.DietaryPreferences

/**
 * Custom RecyclerView adapter that lays out the offers of a given canteen on a given day.
 *
 * @param onClickListener   used for listening to clicks and long presses
 * @param displayAllergens  whether the user wants allergens to be marked
 * @param displayColours    whether the user wants dietary preferences to be marked with colours
 */
class CanteenOfferItemAdapter(
    private val onClickListener: OnClickListener,
    private val displayAllergens: Boolean,
    private val displayColours: Boolean,
) : RecyclerView.Adapter<CanteenOfferItemAdapter.OfferViewHolder>() {

    /**
     * Offers to display.
     */
    private val offers: MutableList<CanteenOfferGroup> = mutableListOf()

    /**
     * Difference between filtered list and the list fetched from the website.
     */
    private var difference: Int = 0

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
                false,
            )
        )
    }

    // Returns the amount of offers for a given canteen and day
    override fun getItemCount(): Int = offers.size

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {

        // Retrieve item for current position
        val currentItem = offers[position]

        // Context for easier reference
        val context = holder.binding.root.context

        holder.binding.canteenCard.visibility = if (currentItem.date == "ALL\$HIDDEN") {
            View.GONE
        } else {
            View.VISIBLE
        }

        // Display
        if (position == (offers.size - 1) && difference > 0) {
            holder.binding.differenceContainer.visibility = View.VISIBLE
            holder.binding.textDifference.text = if (difference == 1) {
                context.getString(R.string.canteen_item_difference_text_one)
            } else {
                context.getString(R.string.canteen_item_difference_text_other, difference)
            }
        } else {
            holder.binding.differenceContainer.visibility = View.GONE
            holder.binding.textDifference.text = ""
        }

        /*
         * Children of the line container MUST be removed before any are added, as otherwise, during
         * recycling, lines will be duplicated when they are scrolled off the visible screen area
         * and then scrolled onto the screen again. In other terms, if removeAllViews() wasn't
         * called and the user was to scroll down a list and then back up again, the items at the
         * top of the list would be added again, creating duplicates.
         */
        holder.binding.lineContainer.removeAllViews()

        // If an item has been added to mark that there are no offers available, tell the user
        if (currentItem.category == "NO\$ITEMS") {

            // Set the text to "No offers" (localised)
            holder.binding.textCategory.text =
                context.getString(R.string.canteen_no_offer_header)

            // Inflate a single line without binding it to the line container
            val line = ItemCanteenLineBinding.inflate(
                LayoutInflater.from(context),
            )

            // Set content to tell the user that no offers are available (localised)
            line.textContent.text =
                context.getString(R.string.canteen_no_offer_desc)

            // Inflate a new icon holder
            val img = ItemIconBinding.inflate(LayoutInflater.from(context))

            // Retrieve data for the dietary preference
            val (_, drawableId, colourId) = DietaryPreferences.getData(
                DietaryPreferences.ERROR.ordinal,
            )

            // Set a cross icon
            img.imageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    drawableId,
                )
            )

            // Set tint of the icon; themed to the preference, if the user has the option enabled
            img.imageView.imageTintList = ColorStateList.valueOf(
                context.theme.getThemedColor(
                    if (displayColours) {
                        colourId
                    } else {
                        R.attr.colorText
                    }
                )
            )

            // Bind the new icon to the line view
            line.imageViewContainer.addView(img.root)

            // Bind the new line to the line container
            holder.binding.lineContainer.addView(line.root)

        } else { // Proceed normally if items are available
            // Set category text / header of the item
            holder.binding.textCategory.text = currentItem.category

            // Create a new view (line) for each item displayed within one category
            currentItem.offers.forEach { offer ->
                // Inflate the new line without binding it to the line container
                val line = ItemCanteenLineBinding.inflate(
                    LayoutInflater.from(context),
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
                if (indices.isEmpty()) indices.add(DietaryPreferences.NONE.ordinal)

                // Iterate through all icons that need to be displayed
                indices.forEach { index ->
                    // Inflate a new icon holder
                    val img = ItemIconBinding.inflate(LayoutInflater.from(context))

                    // Retrieve data for the dietary preference
                    val (_, drawableId, colourId) = DietaryPreferences.getData(index)

                    // Set the appropriate icon
                    img.imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            drawableId,
                        )
                    )

                    // Set tint of the icon
                    img.imageView.imageTintList = ColorStateList.valueOf(
                        context.theme.getThemedColor(
                            if (displayColours) {
                                colourId
                            } else {
                                R.attr.colorText
                            }
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
    }

    /**
     * Updates the data and calculates the difference between the old dataset and the newly provided
     * dataset.
     *
     * @param newData   new dataset to be displayed
     */
    fun setNewData(newData: List<CanteenOfferGroup>, difference: Int) {
        this.difference = difference

        // Calculate the difference between the old list and the new list
        val diffResult = DiffUtil.calculateDiff(AppDiffUtilCallback(offers, newData))

        // Remove all items from the list
        offers.clear()

        // Add all items from the new list
        offers.addAll(newData)

        // Tell the DiffUtil which items have changed between the two lists
        diffResult.dispatchUpdatesTo(this)

        // Force an update on the last item to display how many items have been hidden
        notifyItemChanged(offers.size - 1)
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