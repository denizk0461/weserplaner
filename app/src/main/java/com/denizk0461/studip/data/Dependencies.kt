package com.denizk0461.studip.data

import com.denizk0461.studip.db.AppRepository

/**
 * Static object that holds a reference to the app's repository to provide abstracted access to the
 * app database.
 */
object Dependencies {
    /**
     * Reference to the app's repository. Since it instantiated once by the main activity, it can be
     * accessed statically by all other classes. Used for database transactions.
     */
    lateinit var repo: AppRepository
}