package com.denizk0461.studip.data

import com.denizk0461.studip.model.StudIPEvent
import org.jsoup.Jsoup

class StudIPParser {

    fun parse(html: String, insert: (events: List<StudIPEvent>) -> Unit) {
        var id = 0
        val doc = Jsoup.parse(html)
        val newEvents = mutableListOf<StudIPEvent>()
        val columns = arrayOf(
            doc.getElementById("calendar_view_1_column_0"),
            doc.getElementById("calendar_view_1_column_1"),
            doc.getElementById("calendar_view_1_column_2"),
            doc.getElementById("calendar_view_1_column_3"),
            doc.getElementById("calendar_view_1_column_4"),
        )
        columns.forEachIndexed { index, element ->
            element?.getElementsByClass("schedule_entry")?.forEachIndexed { entryIndex, entry ->
                val entryHeader = entry.getElementsByTag("dt")[0].text()
                val delimiter = entryHeader.lastIndexOf('(')
                val parsedTitle = entryHeader.substring(0 until delimiter)
                val parsedLecturers = entryHeader.substring(delimiter + 1 until entryHeader.length - 1)

                val entryInfos = entry.getElementsByTag("dd")[0].text().split(", ", limit = 2)
                val timeSlot = DummyData.parseTimeslot(entryInfos[0])

                val event = StudIPEvent(
                id = id,
                title = parsedTitle,
                lecturer = parsedLecturers,
                room = entryInfos[1],
                day = index,
                timeslotStart = timeSlot.first,
                timeslotEnd = timeSlot.second,
                )
                newEvents.add(event)
                id += 1
            }
        }
        insert(newEvents)
    }
}