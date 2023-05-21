package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import com.denizk0461.weserplaner.data.StudIPParser
import com.denizk0461.weserplaner.exception.NotLoggedInException
import java.io.IOException
import kotlin.jvm.Throws

/**
 * View model for [com.denizk0461.weserplaner.activity.FetcherActivity]
 *
 * @param app   reference to the app
 */
class FetcherViewModel(app: Application) : AppViewModel(app) {

    /**
     * Instance of the parser used to fetch and parse the user's Stud.IP schedule.
     */
    private val parser: StudIPParser = StudIPParser(app)

    /**
     * Fetches and parses the user's Stud.IP schedule.
     *
     * @param html                  source code of the schedule website
     * @throws NotLoggedInException if the user has not logged in before starting the fetch
     * @throws IOException          when an error in fetching or the database transaction occurs
     */
    @Throws(NotLoggedInException::class, IOException::class)
    fun parse(html: String): Int =
        returnBlocking {
            parser.parse(html)
        }

}