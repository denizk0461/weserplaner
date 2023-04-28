package com.denizk0461.studip.viewmodel

import android.app.Application
import com.denizk0461.studip.data.StudIPParser

/**
 * View model for [com.denizk0461.studip.activity.FetcherActivity]
 *
 * @param app   reference to the app
 */
class FetcherViewModel(app: Application) : AppViewModel(app) {

    private val parser: StudIPParser = StudIPParser(app)

    fun parse(html: String) { doAsync { parser.parse(html, {}) }}
}