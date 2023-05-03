package com.denizk0461.studip.data

import android.app.Application
import com.denizk0461.studip.db.AppRepository
import com.denizk0461.studip.model.StudIPEvent
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException
import kotlin.jvm.Throws

/**
 * Parser class used for fetching and collecting scheduled events from a Stud.IP timetable.
 */
class StudIPParser(application: Application) {

    private val repo: AppRepository = AppRepository.getRepositoryInstance(application)

    /**
     * Parse a given HTML string. HTML must be of a Stud.IP timetable.
     *
     * @param html          website content of the Stud.IP timetable
     * @throws IOException  when an error in fetching or the database transaction occurs
     */
    @Throws(IOException::class)
    fun parse(html: String) {
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
         * Iterate through all columns to parse and assign the events of each day individually and
         * accordingly.
         */
        doc.getDateIndices().forEach { (index, element) ->

            // Iterate through all events scheduled for a given day
            element.getElementsByClass("schedule_entry").forEach { entry ->
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
                    eventId = id,
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

        // Delete all previously saved events
        repo.nukeEvents()

        // After fetching has finished, save the list of new items into persistent storage
        repo.insertEvents(newEvents)
    }

    /**
     * Finds and returns all columns that are present in the given timetable and maps them to their
     * respective date indices (0 = Monday, 4 = Friday, 6 = Sunday). Returns an empty list if no
     * elements could be found.
     *
     * @return  table columns paired with their day indices
     */
    private fun Document.getDateIndices(): List<Pair<Int, Element>> {
        // Create list to insert values into
        val list = mutableListOf<Pair<Int, Element>>()

        // Find headers that include day information for every schedule column
        val headers = getElementById("schedule_data")
            ?.getElementsByTag("thead")
            ?.get(0)
            ?.getElementsByTag("td") ?: return list

        // Iterate through all headers
        for (index in 1 until headers.size) {

            /*
             * Check which days are present. Two strings are provided to ensure this process works
             * no matter whether the user has set their Stud.IP to English or German. Only these two
             * languages because Stud.IP doesn't seem to support any other languages.
             */
            val dayIndex = when (headers[index].text()) {
                "Mon.", "Mo" -> 0
                "Tue.", "Di" -> 1
                "Wed.", "Mi" -> 2
                "Thu.", "Do" -> 3
                "Fri.", "Fr" -> 4
                "Sat.", "Sa" -> 5
                "Sun.", "So" -> 6
                else -> continue
            }

            // Add the days found to the list
            list.add(Pair(
                dayIndex,
                getElementById("calendar_view_1_column_${dayIndex}") ?: continue,
            ))
        }

        // Return the list of columns
        return list
    }
}