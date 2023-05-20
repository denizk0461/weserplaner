package com.denizk0461.weserplaner.viewmodel

import android.app.Application

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
}