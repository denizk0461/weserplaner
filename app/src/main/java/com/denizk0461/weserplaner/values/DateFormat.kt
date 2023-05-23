package com.denizk0461.weserplaner.values

/**
 * Determines the format a date should be put into.
 */
enum class DateFormat(val dateFormat: String, val timeFormat: String) {
    EUROPEAN("dd.MM.yyyy", "H:mm"), // 24.05.2023, 8:45
    ISO8601("yyyy-MM-dd", "HH:mm"), // 2023-05-24, 08:45
    ISO8601_PRECISE("yyyy-MM-dd", "HH:mm:ss.SSS"), // 2023-05-24, 08:45:11.518
    FUCKED_UP("MM/dd/yy", "h:mm a"), // 05/24/23, 8:45 AM
    ;
}