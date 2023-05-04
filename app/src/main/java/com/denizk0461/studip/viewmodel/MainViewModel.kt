package com.denizk0461.studip.viewmodel

import android.app.Application
import com.denizk0461.studip.model.SettingsPreferences

/**
 * View model for [com.denizk0461.studip.activity.MainActivity]
 *
 * @param application   reference to the app
 */
class MainViewModel(application: Application) : AppViewModel(application) {

    /**
     * This value determines whether the user wants the app to launch with the canteen view.
     */
    val preferenceLaunchCanteen: Boolean
        get() = repo.getBooleanPreference(SettingsPreferences.LAUNCH_CANTEEN_ON_START)
}