package com.denizk0461.weserplaner.model

import androidx.room.ColumnInfo

/**
 * Entity that is constructed by combining instances of OfferItem.kt (itemId, title, price,
 * dietaryPreferences), OfferCategory.kt (category, categoryId), OfferCanteen.kt (canteen,
 * canteenId), and OfferDate.kt (date, dateId) to be used as a data type for a custom RecyclerView
 * adapter class.
 *
 * @param itemId                unique identifier of the item
 * @param date                  date formatted as dd.MM.
 * @param dateId                ID of the corresponding OfferDate.kt instance
 * @param category              category text content of the item
 * @param categoryId            ID of the corresponding OfferCategory.kt instance
 * @param canteen               name of the canteen
 * @param canteenId             ID of the corresponding OfferCanteen.kt instance
 * @param title                 text content of the individual canteen offer
 * @param price                 students' price for the individual canteen offer
 * @param dietaryPreferences    dietary preferences used to filter for the user's needs - see
 *                              [DietaryPreferences]
 * @param allergens             allergens and additives that are present in the item
 */
data class CanteenOffer(
    val itemId: Int,
    val date: String,
    val dateId: Int,
    val category: String,
    val categoryId: Int,
    val canteen: String,
    val canteenId: Int,
    val title: String,
    val price: String,
    @ColumnInfo(name = "dietary_preferences") val dietaryPreferences: String,
    val allergens: String,
)
