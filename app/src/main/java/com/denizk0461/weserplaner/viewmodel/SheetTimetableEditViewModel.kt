package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import com.denizk0461.weserplaner.model.Timetable

class SheetTimetableEditViewModel(application: Application) : AppViewModel(application) {

    fun getTimetableForId(id: Int) = returnBlocking { repo.getTimetableForId(id) }

    fun updateTimetable(timetable: Timetable) { doAsync { repo.updateTimetable(timetable) } }

    fun deleteTimetable(id: Int) = doAsync { repo.deleteTimetable(id) }

    fun setSelectedTimetable(newValue: Int) = repo.setPreferenceSelectedTimetable(newValue)

    fun getLargestTimetableId(): Int = returnBlocking { repo.getLargestTimetableId() }
}