package com.denizk0461.studip.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.denizk0461.studip.data.Dependencies
import com.denizk0461.studip.db.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

open class TemplateViewModel(app: Application) : AndroidViewModel(app) {

    protected val repo: EventRepository = Dependencies.repo

    fun doAsync(function: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            function()
        }
    }

    fun <T> returnBlocking(function: () -> T): T = runBlocking(Dispatchers.IO) {
        function()
    }
}