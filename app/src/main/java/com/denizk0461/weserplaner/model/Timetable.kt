package com.denizk0461.weserplaner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Timetable parent object that [StudIPEvent]s are assigned to. These can be named to denote
 * different semesters.
 *
 * @param id    unique primary key value
 * @param name  title of the timetable
 */
@Entity(tableName = "timetables")
data class Timetable(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
)
