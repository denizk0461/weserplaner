package com.denizk0461.weserplaner.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
