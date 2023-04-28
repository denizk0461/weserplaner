package com.denizk0461.studip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for storing entries of the user's Stud.IP schedule. Registered in the app database.
 *
 * @param id            primary key that uniquely identifies the entry
 * @param title         title of the event
 * @param lecturer      lecturer(s) organising the event
 * @param room          room the event takes place in
 * @param day           day the event takes place on; 0 = Monday, 4 = Friday
 * @param timeslotStart time the event starts at
 * @param timeslotEnd   time the event ends at
 * @param timeslotId    minute the event starts at - used for ordering
 * @param colour        user-defined colour the event will be shown in - UNIMPLEMENTED
 */
@Entity(tableName = "studipevents")
data class StudIPEvent(
    @PrimaryKey val id: Int,
    val title: String,
    val lecturer: String,
    val room: String,
    val day: Int,
    val timeslotStart: String,
    val timeslotEnd: String,
    val timeslotId: Int,
    val colour: Int = 0,
) {

    /**
     * Parse timeslotStart and timeslotEnd into an easily readable string in the following format:
     * 12:15 – 13:45
     *
     * @return formatted time string
     */
    fun timeslot(): String = "$timeslotStart – $timeslotEnd"

    // Auto-generated methods. Necessary for [AppDiffUtilCallback]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StudIPEvent

        if (id != other.id) return false
        if (title != other.title) return false
        if (lecturer != other.lecturer) return false
        if (room != other.room) return false
        if (day != other.day) return false
        if (timeslotStart != other.timeslotStart) return false
        if (timeslotEnd != other.timeslotEnd) return false
        if (timeslotId != other.timeslotId) return false
        if (colour != other.colour) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + lecturer.hashCode()
        result = 31 * result + room.hashCode()
        result = 31 * result + day
        result = 31 * result + timeslotStart.hashCode()
        result = 31 * result + timeslotEnd.hashCode()
        result = 31 * result + timeslotId
        result = 31 * result + colour
        return result
    }
}
