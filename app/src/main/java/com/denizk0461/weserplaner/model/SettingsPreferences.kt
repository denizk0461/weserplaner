package com.denizk0461.weserplaner.model

/**
 * Enumeration class for user-set preferences that will be saved in persistent storage via
 * SharedPreferences.
 * TODO mark which data types should be used for which preferences (boolean, string, int)
 *
 * @param key   key for the SharedPreferences transaction
 */
enum class SettingsPreferences(val key: String) {

    /**
     * Whether the user is launching this app for the first time.
     */
    FIRST_LAUNCH("check_first_launch"),

    /**
     * Which allergens the user wants displayed or hidden.
     */
    ALLERGEN_CONFIG("setting_allergen_config"),

    /**
     * Whether the user wants to have allergens displayed in the canteen overview.
     */
    ALLERGEN("setting_allergen"),

    /**
     * Whether the user wants their timetable on the current day.
     */
    CURRENT_DAY("settings_current_day"),

    /**
     * Whether the user wants the next course in his schedule to be highlighted.
     */
    COURSE_HIGHLIGHTING("setting_highlight"),

    /**
     * Which prices the user wants to have displayed for the canteen offers.
     */
    PRICING("settings_pricing"),

    /**
     * Which fragment the user wants the app to start with.
     */
    LAUNCH_FRAGMENT_ON_START("settings_launch_fragment"),

    /**
     * Whether the user wants dietary preferences to be coloured.
     */
    COLOUR_PREFS("settings_prefs_colour"),

    /**
     * Whether the user opts into sending crash report.
     */
    DATA_HANDLING("setting_data_handling"),

    /**
     * The canteen the user has picked.
     */
    CANTEEN("setting_canteen"),

    /**
     * Whether the user has opened the canteen fragment before.
     */
    HAS_OPENED_CANTEEN("has_opened_canteen"),

    /**
     * Whether the user has enabled experimental settings.
     */
    EXPERIMENTAL_ENABLED("experimental_enabled"),
    ;
}