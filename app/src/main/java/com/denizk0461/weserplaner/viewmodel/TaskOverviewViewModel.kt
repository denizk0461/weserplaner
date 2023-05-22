package com.denizk0461.weserplaner.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.denizk0461.weserplaner.model.EventTask
import com.denizk0461.weserplaner.model.TaskOrder

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
        EventTask(0, 0, 1684758756, "title1", "user notes", "GW2 B-idk"),
        EventTask(1, 0, 1684798756, "title2", "user notes", "GW3 C-idk"),
        EventTask(2, 0, 1689758756, "title3", "user notes", "GW4 D-idk"),
    )
}