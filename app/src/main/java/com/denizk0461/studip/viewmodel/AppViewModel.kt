package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.denizk0461.studip.db.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * View model super class providing common functionality. View model is used to provide an
 * abstraction between the view classes and the data providers. All view models should inherit from
 * this.
 *
 * @param app   reference to the app
 */
open class AppViewModel(app: Application) : AndroidViewModel(app) {

    /**
     * Reference to the app's repository for database transactions.
     */
    protected val repo: AppRepository = AppRepository.getRepositoryInstance(app)

    /**
     * Execute a function asynchronously on the I/O thread. Be careful not to execute UI commands
     * with this.
     *
     * @param function  the action that will be executed asynchronously
     */
    fun doAsync(function: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            function()
        }
    }

    /**
     * Execute a function that needs to be ran on an I/O thread and return a value. This is a simple
     * but inefficient solution to retrieving data from the database without the need for a LiveData
     * object, which should always be preferred.
     *
     * @param function  the action that will be executed on the I/O thread
     * @return          any value returned by the executing function
     */
    fun <T> returnBlocking(function: () -> T): T = runBlocking(Dispatchers.IO) {
        function()
    }
}