package com.denizk0461.studip.data

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.denizk0461.studip.R
import com.denizk0461.studip.exception.AcademicQuarterNotApplicableException
import com.denizk0461.studip.exception.ParcelNotFoundException
import com.denizk0461.studip.sheet.TextSheet
import com.google.android.material.snackbar.Snackbar
import kotlin.jvm.Throws

/**
 * Miscellaneous functions and variables that don't belong into any specific class.
 */

/**
 * Converts a time stamp string to its numeric value in minutes.
 * Example: 13:20. (20 minutes) + (13 hours * 60 minutes) = 800 minutes.
 *
 * @return  minute value of the string
 */
fun String.parseToMinutes(): Int {
    val parts = split(":")
    return (parts[0].toInt() * 60) + parts[1].toInt()
}

/**
 * Retrieves a specified colour customised to the currently applied theme.
 *
 * @param id    attribute ID of the colour reference
 * @return      resolved colour
 */
fun Resources.Theme.getThemedColor(@AttrRes id: Int): Int = TypedValue().also { value ->
    resolveAttribute(id, value, true)
}.data

/**
 * Present the user with a toast.
 *
 * @param text  text to display
 */
fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun Resources.Theme.showErrorSnackBar(view: CoordinatorLayout, text: String, anchor: View? = null) {
    val s = Snackbar
        .make(view, text, Snackbar.LENGTH_SHORT)
        // Set colours to signify an error
        .setBackgroundTint(getThemedColor(R.attr.colorErrorContainer))
        .setTextColor(getThemedColor(R.attr.colorOnErrorContainer))

    anchor?.let { a -> s.setAnchorView(a) }
    s.show()
}

/**
 * Retrieve a [TextSheet] with text content added as arguments bundle.
 *
 * @param header    header of the sheet
 * @param content   text content of the sheet
 * @return          sheet with text added
 */
fun getTextSheet(header: String, content: String): TextSheet = TextSheet().also { sheet ->
    val bundle = Bundle()
    bundle.putString("header", header)
    bundle.putString("content", content)
    sheet.arguments = bundle
}

/**
 * Retrieves a Parcelable object from a given instance of [Bundle].
 *
 * @param T                         type of the object to retrieve
 * @param key                       string key to retrieve the object from the [Bundle]
 * @throws ParcelNotFoundException  if the Parcel couldn't be retrieved
 */
@Throws(ParcelNotFoundException::class)
inline fun <reified T : Parcelable> Bundle?.getParcelableCompat(key: String): T =
    /*
     * Since the old getParcelable() call is deprecated since API 33 (Tiramisu) and replaced with a
     * type-safe version, and no AppCompat version is available as of now, this API level check is
     * necessary instead.
     */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this?.getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        this?.getParcelable(key) as? T
    } ?: throw ParcelNotFoundException()

/**
 * Applies a rainbow colour effect to a progress circle of a given [SwipeRefreshLayout]. The circle
 * will rotate through 4 different colours during the refresh process.
 */
fun SwipeRefreshLayout.setRainbowProgressCircle() {
    setColorSchemeColors(
        context.getColor(R.color.beef_light_primary),
        context.getColor(R.color.fair_light_primary),
        context.getColor(R.color.vital_light_primary),
        context.getColor(R.color.poultry_dark_primary),
    )
}

/**
 * Provides a conversion method between a timestamp ending in a full hour, and one with an academic
 * quarter applied.
 */
val timeslotsAcademicQuarter: List<String> = listOf(
    "0:00",
    "2:00",
    "4:00",
    "6:00",
    "8:00",
    "10:00",
    "12:00",
    "14:00",
    "16:00",
    "18:00",
    "20:00",
    "22:00",
    "24:00",
)

/**
 * Provides a means of converting from a timestamp ending in a full hour to a timestamp that takes
 * the academic quarter into account. Should only be used on events where it can be assumed that the
 * academic quarter was implied in the timestamp. Use this for the beginning of an event.
 *
 * @param input timestamp to apply the academic quarter to
 * @return      timestamp with the academic quarter applied to it
 */
@Throws(AcademicQuarterNotApplicableException::class)
fun getTimestampAcademicQuarterStart(input: String): String = when (input) {
    "0:00" -> "0:15"
    "2:00" -> "2:15"
    "4:00" -> "4:15"
    "6:00" -> "6:15"
    "8:00" -> "8:15"
    "10:00" -> "10:15"
    "12:00" -> "12:15"
    "14:00" -> "14:15"
    "16:00" -> "16:15"
    "18:00" -> "18:15"
    "20:00" -> "20:15"
    "22:00" -> "22:15"
    "24:00" -> "0:15"
    else -> throw AcademicQuarterNotApplicableException()
}

/**
 * Provides a means of converting from a timestamp ending in a full hour to a timestamp that takes
 * the academic quarter into account. Should only be used on events where it can be assumed that the
 * academic quarter was implied in the timestamp. Use this for the end of an event.
 *
 * @param input timestamp to apply the academic quarter to
 * @return      timestamp with the academic quarter applied to it
 */
@Throws(AcademicQuarterNotApplicableException::class)
fun getTimestampAcademicQuarterEnd(input: String): String = when (input) {
    "0:00" -> "23:45"
    "2:00" -> "1:45"
    "4:00" -> "3:45"
    "6:00" -> "5:45"
    "8:00" -> "7:45"
    "10:00" -> "9:45"
    "12:00" -> "11:45"
    "14:00" -> "13:45"
    "16:00" -> "15:45"
    "18:00" -> "17:45"
    "20:00" -> "19:45"
    "22:00" -> "21:45"
    "24:00" -> "23:45"
    else -> throw AcademicQuarterNotApplicableException()
}