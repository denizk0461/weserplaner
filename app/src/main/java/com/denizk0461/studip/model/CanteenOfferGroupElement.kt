package com.denizk0461.studip.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Stripped version of CanteenOffer.kt that only carries essential info. Assumes that instances of
 * this are assigned to corresponding date, canteen, and category via an instance of
 * CanteenOfferGroup.kt.
 *
 * @param title                 text content of the individual canteen offer
 * @param price                 students' price for the individual canteen offer
 * @param dietaryPreferences    dietary preferences used to filter for the user's needs - see
 *                              [DietaryPreferences]
 * @param allergens             allergens and additives that are present in the item
 */
data class CanteenOfferGroupElement(
    // parcel
    val title: String,
    val price: String,
    val dietaryPreferences: String,
    val allergens: String,
) : Parcelable {

    // Generated Parcelable implementation
    constructor(source: Parcel) : this(
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int): Unit = with(dest) {
        writeString(title)
        writeString(price)
        writeString(dietaryPreferences)
        writeString(allergens)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CanteenOfferGroupElement> =
            object : Parcelable.Creator<CanteenOfferGroupElement> {
                override fun createFromParcel(source: Parcel): CanteenOfferGroupElement =
                    CanteenOfferGroupElement(source)

                override fun newArray(size: Int): Array<CanteenOfferGroupElement?> =
                    arrayOfNulls(size)
            }
    }
}
