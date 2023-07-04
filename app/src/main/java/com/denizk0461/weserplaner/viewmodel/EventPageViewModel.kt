package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.weserplaner.model.StudIPEvent

/**
 * View model for [com.denizk0461.weserplaner.fragment.EventPageFragment]
 *
 * @param app   reference to the app
 */
class EventPageViewModel(app: Application) : AppViewModel(app) {

    /**
     * Retrieves Stud.IP events for a specific day, ordered by their timeslots, then their IDs.
     *
     * @return  Stud.IP events for a certain day exposed through a LiveData object
     */
    fun getEventsForDay(day: Int): LiveData<List<StudIPEvent>> = repo.getEventsForDay(day)
    fun getEventsForDayAndTimetable(day: Int, timetable: Int): LiveData<List<StudIPEvent>> =
        repo.getEventsForDayAndTimetable(day, timetable)

    fun getSelectedTimetable(): Int = repo.getPreferenceSelectedTimetable()

    /**
     * This value determines whether the user wants to have the next course in their schedule
     * highlighted.
     */
    val preferenceCourseHighlighting: Boolean
        get() = repo.getPreferenceCourseHighlighting()
}