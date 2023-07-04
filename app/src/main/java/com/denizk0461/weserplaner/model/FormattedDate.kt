package com.denizk0461.weserplaner.model

import android.content.Context
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.values.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Wrapper for Java's date object that exposes pre-formatted date and time strings.
 *
 * @param date      date object to wrap
 * @param format    format the date should be presented in; see [com.denizk0461.weserplaner.values.DateFormat]
 */
class FormattedDate(val date: Date, format: DateFormat = DateFormat.ISO8601) {

    constructor(time: Long, format: DateFormat = DateFormat.ISO8601) : this(Date(time), format)

    var dateString: String = SimpleDateFormat(format.dateFormat, Locale.GERMANY).format(date)
    var timeString: String = SimpleDateFormat(format.timeFormat, Locale.GERMANY).format(date)

    /**
     * Produces a string containing date and time of the object, separated by a comma. Example:
     * 24.05.2023, 8:45
     *
     * @return          formatted string
     */
    fun commaSeparatedString(): String = "$dateString, $timeString"

    /**
     * Produces a string containing date and time of the object, localised to the user's locale. Example:
     * 24.05.2023 at 8:45 (English)
     * 24.05.2023 um 8:45 (German)
     *
     * @param context   used to retrieve the string resource
     * @return          formatted string
     */
    fun localisedString(context: Context): String = context.getString(
        R.string.formatted_date_string_localised,
        dateString,
        timeString,
    )
}