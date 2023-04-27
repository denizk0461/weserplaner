package com.denizk0461.studip.data

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import com.denizk0461.studip.exception.AcademicQuarterNotApplicableException
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