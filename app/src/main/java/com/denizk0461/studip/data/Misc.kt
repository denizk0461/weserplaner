package com.denizk0461.studip.data

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import com.denizk0461.studip.R

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
val timeslotsAcademicQuarter = listOf(
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
)

val timeslotsAcademicQuarterStart = mapOf(
    "4:00" to "4:15",
    "6:00" to "6:15",
    "8:00" to "8:15",
    "10:00" to "10:15",
    "12:00" to "12:15",
    "14:00" to "14:15",
    "16:00" to "16:15",
    "18:00" to "18:15",
    "20:00" to "20:15",
)

val timeslotsAcademicQuarterEnd = mapOf(
    "6:00" to "5:45",
    "8:00" to "7:45",
    "10:00" to "9:45",
    "12:00" to "11:45",
    "14:00" to "13:45",
    "16:00" to "15:45",
    "18:00" to "17:45",
    "20:00" to "19:45",
    "22:00" to "21:45",
)