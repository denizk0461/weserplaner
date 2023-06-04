package com.denizk0461.weserplaner.viewmodel

import android.app.Application

/**
 * View model for [com.denizk0461.weserplaner.activity.IntroductionActivity].
 */
class IntroductionViewModel(application: Application) : AppViewModel(application) {

    /**
     * Save that the user has launched the app and completed the introduction.
     */
    fun completedIntroduction() {
        repo.setPreferenceFirstLaunch(false)
    }
}