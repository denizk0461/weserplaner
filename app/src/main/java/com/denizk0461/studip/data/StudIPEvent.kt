package com.denizk0461.studip.data

data class StudIPEvent(
    val title: String, // the name of the event
    val lecturer: Array<String>, // the lecturer(s)
    val room: String, // the room the event takes place in; assumes that the room doesn't change
    val timeStart: Int, // 1 = 08:15, 2 = 10:15, 3 = 12:15, 4 = 14:15, 5 = 16:15, 6 = 18:15
    val timeLength: Int, // the amount of hours the course takes place over
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StudIPEvent

        if (title != other.title) return false
        if (!lecturer.contentEquals(other.lecturer)) return false

        return true
    }
    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + lecturer.contentHashCode()
        return result
    }
}
