package com.denizk0461.studip.data

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

object Misc {

//    private val timeslotAcademicQuarter = mapOf(
//        "6:00" to "6:15",
//        "8:00" to "7:45",
//        "8:00" to "8:15",
//        "10:00" to "9:45",
//        "10:00" to "10:15",
//        "12:00" to "11:45",
//        "12:00" to "12:15",
//        "14:00" to "13:45",
//        "14:00" to "14:15",
//        "16:00" to "15:45",
//        "16:00" to "16:15",
//        "18:00" to "17:45",
//        "18:00" to "18:15",
//        "20:00" to "19:45",
//        "20:00" to "20:15",
//        "22:00" to "21:45",
//    )
}