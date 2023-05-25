package com.denizk0461.weserplaner.model

/**
 * Extended representation of [com.denizk0461.weserplaner.model.EventTask] that includes important
 * attributes from a given task's associated [com.denizk0461.weserplaner.model.StudIPEvent].
 *
 * @param taskId        primary key for the entity
 * @param eventId       foreign key referencing the event this task is assigned to
 * @param dueDate       time and date at which the task is due
 * @param notifyDate    time and date at which the user wants to be notified about this task; -1 if
 *                      the user doesn't want to be notified
 * @param eventTitle    title of the event
 * @param taskTitle     title of the task
 * @param lecturer      lecturer(s) organising the event
 * @param notes         user-added notes for the task
 * @param room          room the task may take place in
 * @param isFinished    whether this task has been marked as finished by the user
 */
data class EventTaskExtended(
    val taskId: Int,
    val eventId: Int,
    val dueDate: Long,
    val notifyDate: Long,
    val eventTitle: String,
    val taskTitle: String,
    val lecturer: String,
    val notes: String,
    val room: String,
    val isFinished: Boolean,
)
