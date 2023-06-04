package com.denizk0461.weserplaner.viewmodel

import android.app.Application

/**
 * View model class for [com.denizk0461.weserplaner.sheet.EventActionSheet].
 */
class EventActionViewModel(application: Application) : AppViewModel(application) {

    /**
     * This value determines whether the user has enabled experimental settings.
     */
    val preferenceBetaScreensEnabled: Boolean
        get() = repo.getPreferenceBetaScreensEnabled()
}