package com.denizk0461.studip.data

import com.denizk0461.studip.model.StudIPEvent
import org.jsoup.Jsoup

/**
 * Parser class used for fetching and collecting scheduled events from a Stud.IP timetable.
 */
class StudIPParser {

    /**
     * Parse a given HTML string. HTML must be of a Stud.IP timetable.
     *
     * @param html      website content of the Stud.IP timetable
     * @param insert    action call to save the contents to persistent storage
     */
    fun parse(html: String, insert: (events: List<StudIPEvent>) -> Unit) {
        // Primary key value to uniquely identify entries in the database
        var id = 0

        // Parse the HTML using Jsoup to traverse the document
        val doc = Jsoup.parse(html)

        /*
         * Create the list that all events will be added to temporarily before saving them to
         * persistent storage.
         */
        val newEvents = mutableListOf<StudIPEvent>()

        /*
         * Fetch all columns from Monday through Friday to parse and assign the events of each day
         * individually and accordingly.
         * TODO this must be dynamic. As it is, it assumes that the first day is always Monday,
         *  which needs not be the case. The timetable can be customised by the user by removing
         *  days. Also, Sunday is treated as the first day in Stud.IP FOR SOME REASON, so the app
         *  needs to handle this appropriately.
         */
        val columns = arrayOf(
            doc.getElementById("calendar_view_1_column_0"),
            doc.getElementById("calendar_view_1_column_1"),
            doc.getElementById("calendar_view_1_column_2"),
            doc.getElementById("calendar_view_1_column_3"),
            doc.getElementById("calendar_view_1_column_4"),
        )

        // Iterate through each day
        columns.forEachIndexed { index, element ->

            // Iterate through all events scheduled for a given day
            element?.getElementsByClass("schedule_entry")?.forEach { entry ->
                /*
                 * Retrieve the event's header. This will contain the event's title as well as the
                 * lecturers holding the event.
                 */
                val entryHeader = entry.getElementsByTag("dt")[0].text()

                /*
                 * Find out the index before which the title ends and after which the lecturer
                 * name(s) begin.
                 */
                val delimiter = entryHeader.lastIndexOf('(')

                // Retrieve the title of the event from the header string
                val parsedTitle = entryHeader.substring(0 until delimiter).trim()

                // Retrieve the lecturer name(s) from the header string
                val parsedLecturers = entryHeader.substring(delimiter + 1 until entryHeader.length - 1).trim()

                /*
                 * Retrieve further event information. This will contain both the time slot the
                 * event is assigned to (index 0), as well as the room the event will primarily take
                 * place in (index 1).
                 */
                val entryInfo = entry.getElementsByTag("dd")[0].text().split(", ", limit = 2)

                // Retrieve the time slot and split it into start and end time stamps
                val timeSlot = entryInfo[0].split(" - ")

                // Time the event starts at
                val eventStart = timeSlot[0]

                // Construct the newly scraped Stud.IP event
                val event = StudIPEvent(
                    id = id,
                    title = parsedTitle,
                    lecturer = parsedLecturers,
                    room = entryInfo[1],
                    day = index,
                    timeslotStart = eventStart,
                    timeslotEnd = timeSlot[1],
                    timeslotId = eventStart.parseToMinutes(),
                )

                // Add the new element to the temporary list
                newEvents.add(event)

                // Raise the primary key value by 1 to avoid not fulfilling the unique constraint
                id += 1
            }
        }

        // After fetching has finished, save the list of new items into persistent storage
        insert(newEvents)
    }
}