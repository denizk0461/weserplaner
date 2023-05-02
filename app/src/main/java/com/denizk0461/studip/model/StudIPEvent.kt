package com.denizk0461.studip.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for storing entries of the user's Stud.IP schedule. Registered in the app database.
 *
 * @param eventId       primary key that uniquely identifies the entry
 * @param title         title of the event
 * @param lecturer      lecturer(s) organising the event
 * @param room          room the event takes place in
 * @param day           day the event takes place on; 0 = Monday, 4 = Friday
 * @param timeslotStart time the event starts at
 * @param timeslotEnd   time the event ends at
 * @param timeslotId    minute the event starts at - used for ordering
 * @param colour        user-defined colour the event will be shown in - UNIMPLEMENTED
 */
@Entity(tableName = "studip_events")
data class StudIPEvent(
    @PrimaryKey val eventId: Int,
    val title: String,
    val lecturer: String,
    val room: String,
    val day: Int,
    val timeslotStart: String,
    val timeslotEnd: String,
    val timeslotId: Int,
    val colour: Int = 0,
) : Parcelable {
    /**
     * Parse timeslotStart and timeslotEnd into an easily readable string in the following format:
     * 12:15 – 13:45
     *
     * @return formatted time string
     */
    fun timeslot(): String = "$timeslotStart – $timeslotEnd"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StudIPEvent

        if (eventId != other.eventId) return false
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
        var result = eventId
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

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readInt(),
        source.readString() ?: "",
        source.readString() ?: "",
        source.readInt(),
        source.readInt(),
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int): Unit = with(dest) {
        writeInt(eventId)
        writeString(title)
        writeString(lecturer)
        writeString(room)
        writeInt(day)
        writeString(timeslotStart)
        writeString(timeslotEnd)
        writeInt(timeslotId)
        writeInt(colour)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<StudIPEvent> = object : Parcelable.Creator<StudIPEvent> {
            override fun createFromParcel(source: Parcel): StudIPEvent = StudIPEvent(source)
            override fun newArray(size: Int): Array<StudIPEvent?> = arrayOfNulls(size)
        }
    }
}
