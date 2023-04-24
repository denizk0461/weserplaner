package com.denizk0461.studip.model

/**
 * Enumeration class for user-set preferences that will be saved in persistent storage via
 * SharedPreferences.
 *
 * @param key   key for the SharedPreferences transaction
 */
enum class SettingsPreferences(val key: String) {
    // Whether the user wants to have allergens displayed in the canteen overview
    ALLERGEN("setting_allergen"),
    // Whether the user wants the next course in his schedule to be highlighted
    COURSE_HIGHLIGHTING("setting_highlight"),
    // Whether the user opts into sending crash report
    DATA_HANDLING("setting_data_handling"),
    ;
}