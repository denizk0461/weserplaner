package com.denizk0461.weserplaner.data

import android.app.Application
import com.denizk0461.weserplaner.db.AppRepository
import com.denizk0461.weserplaner.exception.NotLoggedInException
import com.denizk0461.weserplaner.model.FormattedDate
import com.denizk0461.weserplaner.model.StudIPEvent
import com.denizk0461.weserplaner.model.Timetable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException
import java.util.Date
import kotlin.jvm.Throws

/**
 * Parser class used for fetching and collecting scheduled events from a Stud.IP timetable.
 */
class StudIPParser(application: Application) {

    private val repo: AppRepository = AppRepository.getRepositoryInstance(application)

    /**
     * Parse a given HTML string. HTML must be of a Stud.IP timetable. May skip elements that don't
     * provide full information on an event (title, timeslot, lecturers, room).
     *
     * @param html                  website content of the Stud.IP timetable
     * @throws NotLoggedInException if the user has not logged in before starting the fetch
     * @throws IOException          when an error in fetching or the database transaction occurs
     */
    @Throws(NotLoggedInException::class, IOException::class)
    fun parse(html: String): Int {

        // Counts how many elements could not be successfully fetched
        var elementsNotFetched = 0

        // Parse the HTML using Jsoup to traverse the document
        val doc = Jsoup.parse(html)

        // Check if the user has logged in by checking the ID of the body tag
        if (doc.body().id() != "calendar-schedule-index") {
            throw NotLoggedInException()
        }

        val timetableId = repo.getLargestTimetableId() + 1

        repo.insertTimetable(
            Timetable(timetableId, FormattedDate(Date(System.currentTimeMillis())).commaSeparatedString())
        )

        repo.setPreferenceSelectedTimetable(timetableId)

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
            element.getElementsByClass("schedule_entry").forEach loop@{ entry ->
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
                val parsedTitle = try {
                    entryHeader.substring(0 until delimiter).trim()
                } catch (e: StringIndexOutOfBoundsException) {
                    elementsNotFetched += 1
                    return@loop
                }

                // Retrieve the lecturer name(s) from the header string
                val parsedLecturers = try {
                    entryHeader
                        .substring(delimiter + 1 until entryHeader.length - 1).trim()
                } catch (e: StringIndexOutOfBoundsException) {
                    elementsNotFetched += 1
                    return@loop
                }

                /*
                 * Retrieve further event information. This will contain both the time slot the
                 * event is assigned to (index 0), as well as the room the event will primarily take
                 * place in (index 1).
                 */
                val entryInfo = entry.getElementsByTag("dd")[0].text().split(", ", limit = 2)

                // Retrieve the time slot and split it into start and end time stamps
                val timeSlot = entryInfo[0].split(" - ")

                // Time the course starts at
                val eventStart = timeSlot.getOrQuestionMark(0)

                // Construct the newly scraped Stud.IP event
                val event = StudIPEvent(
                    timetableId = timetableId,
                    title = parsedTitle,
                    lecturer = parsedLecturers,
                    room = entryInfo.getOrQuestionMark(1),
                    day = index,
                    timeslotStart = eventStart,
                    timeslotEnd = timeSlot.getOrQuestionMark(1),
                    timeslotId = eventStart.parseToMinutes(),
                )

                // Add the new element to the temporary list
                newEvents.add(event)
            }
        }

        // After fetching has finished, save the list of new items into persistent storage
        repo.insertEvents(newEvents)

        // Return the count of elements that were not able to be fetched
        return elementsNotFetched
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
        val headers = getElementById("schedule_data")?.let {
            it.getElementsByTag("thead")[0].getElementsByTag("td")
        } ?: return list

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
                /*
                 * The index of the columns doesn't seem to depend on the other elements in the
                 * table. Friday will always have the index 4 ("calendar_view_1_column_4") even if
                 * Monday through Thursday are hidden from the schedule.
                 */
                getElementById("calendar_view_1_column_${dayIndex}") ?: continue,
            ))
        }

        // Return the list of columns
        return list
    }

    private fun List<String>.getOrQuestionMark(index: Int): String = try {
        get(index)
    } catch (e: IndexOutOfBoundsException) {
        "?"
    }
}