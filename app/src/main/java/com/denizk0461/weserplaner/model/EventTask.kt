package com.denizk0461.weserplaner.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Database entity used to store tasks assigned to specific [StudIPEvent] elements.
 *
 * @param taskId    primary key for the entity
 * @param eventId   foreign key referencing the event this task is assigned to
 * @param dueDate   time and date at which the task is due
 * @param title     title of the task
 * @param notes     user-added notes for the task
 * @param room      room the task may take place in
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
    val dueDate: Int,
    val title: String,
    val notes: String,
    val room: String,
)
