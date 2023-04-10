package com.denizk0461.studip.model

import android.util.Log
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class StudIPEvent(
    @PrimaryKey val id: Int,
    val title: String, // the name of the event
    val lecturer: String, // the lecturer(s)
    val room: String, // the room the event takes place in
    val day: Int, // 0 = Monday, 1 = Tuesday, 2 = Wednesday, 3 = Thursday, 4 = Friday
    val timeslotStart: Int, // 1 = 08:15, 2 = 10:15, 3 = 12:15, 4 = 14:15, 5 = 16:15, 6 = 18:15
    val timeslotEnd: Int, // 1 = 09:45 etc.
) {

//    constructor(
//        val title: String,
//        val lecturer: String,
//        val room: String,
//        val day: Int,
//        val
//    ) : this(title, lecturer, room, day, 1, 1)

    @Ignore private val timeSlotStartTimes: List<String> = listOf("06:15", "08:15", "10:15", "12:15", "14:15", "16:15", "18:15")
    @Ignore private val timeSlotEndTimes: List<String> = listOf("07:45", "09:45", "11:45", "13:45", "15:45", "17:45", "19:45")

    fun timeslot(): String {
        Log.d("HELLO3", title)
        Log.d("HELLO3", "s $timeslotStart st $timeslotEnd")
        return "${timeSlotStartTimes[timeslotStart]} – ${timeSlotEndTimes[timeslotEnd]}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StudIPEvent

        if (title != other.title) return false
        if (!lecturer.contentEquals(other.lecturer)) return false

        return true
    }
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + lecturer.hashCode()
        result = 31 * result + room.hashCode()
        result = 31 * result + day
        result = 31 * result + timeslotStart
        result = 31 * result + timeslotEnd
        result = 31 * result + timeSlotStartTimes.hashCode()
        result = 31 * result + timeSlotEndTimes.hashCode()
        return result
    }
}
