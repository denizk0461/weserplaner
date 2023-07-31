package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.denizk0461.weserplaner.model.Timetable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SheetTimetableAddViewModel(application: Application) : AppViewModel(application) {

    fun insertTimetable(timetable: Timetable) { viewModelScope.launch(Dispatchers.IO) { repo.insertTimetable(timetable) }}
}