package com.denizk0461.weserplaner.model

/**
 * Extended representation of [com.denizk0461.weserplaner.model.EventTask] that includes important
 * attributes from a given task's associated [com.denizk0461.weserplaner.model.StudIPEvent].
 *
 * @param taskId        primary key for the entity
 * @param eventId       foreign key referencing the event this task is assigned to
 * @param dueDate       time and date at which the task is due
 * @param eventTitle    title of the event
 * @param taskTitle     title of the task
 * @param lecturer      lecturer(s) organising the event
 * @param notes         user-added notes for the task
 * @param room          room the task may take place in
 */
data class EventTaskExtended(
    val taskId: Int,
    val eventId: Int,
    val dueDate: Int,
    val eventTitle: String,
    val taskTitle: String,
    val lecturer: String,
    val notes: String,
    val room: String,
)
