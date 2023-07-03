package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import com.denizk0461.weserplaner.model.Timetable
import com.denizk0461.weserplaner.values.AppLayout

/**
 * View model for [com.denizk0461.weserplaner.activity.MainActivity]
 *
 * @param application   reference to the app
 */
class MainViewModel(application: Application) : AppViewModel(application) {

    /**
     * This value determines which fragment the user wants the app to start with. Order is equal to
     * the order that the items are arranged in in the bottom nav bar.
     */
    val preferenceLaunchFragment: Int
        get() = repo.getPreferenceLaunchFragment()

    /**
     * This value determines whether the user has enabled experimental settings.
     */
    val preferenceBetaScreensEnabled: Boolean
        get() = repo.getPreferenceBetaScreensEnabled()

    /**
     * Determines whether the user is launching the app for the first time.
     */
    val preferenceFirstLaunch: Boolean
        get() = repo.getPreferenceFirstLaunch()

    /**
     * Determines which layout the user wants the app to use.
     */
    val preferenceAppLayout: AppLayout
        get() = repo.getAppLayout()
}