package com.denizk0461.weserplaner.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Database entity used to store tasks assigned to specific [StudIPEvent] elements.
 *
 * @param taskId        primary key for the entity
 * @param eventId       foreign key referencing the event this task is assigned to
 * @param dueDate       time and date at which the task is due
 * @param notifyDate    time and date at which the user wants to be notified about this task; -1 if
 *                      the user doesn't want to be notified
 * @param title         title of the task
 * @param notes         user-added notes for the task
 * @param room          room the task may take place in
 * @param isFinished    whether this task has been marked as finished by the user
 */
@Entity(
    tableName = "event_tasks",
    foreignKeys = [
        ForeignKey(
            StudIPEvent::class,
            ["eventId"],
            ["eventId"],
            ForeignKey.CASCADE,
        ),
    ],
)
data class EventTask(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val eventId: Int,
    val dueDate: Long,
    val notifyDate: Long,
    val title: String,
    val notes: String,
    val room: String,
    val isFinished: Boolean = false,
)
