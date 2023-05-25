package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import com.denizk0461.weserplaner.model.EventTaskExtended
import com.denizk0461.weserplaner.values.TaskOrder

/**
 * View model class for [com.denizk0461.weserplaner.fragment.TaskOverviewFragment].
 */
class TaskOverviewViewModel(application: Application) : AppViewModel(application) {

    /**
     * Retrieves tasks for all events ordered as specified.
     *
     * @param order order in which the tasks are returned
     * @return      tasks wrapped in a LiveData object
     */
//    fun getTasks(order: TaskOrder): LiveData<List<EventTask>> = repo.getTasks(order)

    fun getTasks(order: TaskOrder) = listOf( // dummy data
        EventTaskExtended(0, 0, 1684758756L, -1L, "event1", "title1", "lecturer", "user notes", "GW2 B-idk", false),
        EventTaskExtended(1, 0, 1684798756L, -1L, "event2", "title2", "lecturer", "user notes", "GW3 C-idk", false),
        EventTaskExtended(2, 0, 1689758756L, 1689958756L, "event3", "title3", "lecturer", "user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes user notes", "GW4 D-idk", false),
    )
}