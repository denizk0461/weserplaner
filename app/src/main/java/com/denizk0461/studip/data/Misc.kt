package com.denizk0461.studip.data

import com.denizk0461.studip.model.StudIPEvent

object Misc {

//    val events: Array<StudIPEvent> = arrayOf(
//        StudIPEvent(1, "Literatures", "Nittel", "MZH 1380/1400", 0, 3, 1),
//        StudIPEvent(2, "Linguistics", "Du Bois", "SFG 1020", 1, 1, 1),
//        StudIPEvent(3, "System Erde", "Zolitschka, Labuhn-Deroubaix", "GW2 B1410", 1, 3, 1),
//        StudIPEvent(4, "Key Moments", "Esders-Angermund", "GW2 B2890", 2, 2, 1),
//        StudIPEvent(5, "ULS-1", "Herrmann", "GW2 B2890", 2, 3, 1),
//        StudIPEvent(6, "Gesellschaft und Raum", "Lossau, Mossig", "GW2 B1820", 3, 1, 1),
//        StudIPEvent(7, "Gesellschaft und Raum2", "Lossau, Mossig", "GW2 B1820", 4, 4, 2),
//    )

    private val timeslotStartMap: Map<String, Int> = mapOf(
        "6:00" to 0,
        "6:15" to 0,
        "8:00" to 1,
        "8:15" to 1,
        "10:00" to 2,
        "10:15" to 2,
        "12:00" to 3,
        "12:15" to 3,
        "14:00" to 4,
        "14:15" to 4,
        "16:00" to 5,
        "16:15" to 5,
        "18:00" to 6,
        "18:15" to 6,
        "20:00" to 7,
        "20:15" to 7,
    )

    private val timeslotEndMap: Map<String, Int> = mapOf(
        "8:00" to 0,
        "7:45" to 0,
        "10:00" to 1,
        "9:45" to 1,
        "12:00" to 2,
        "11:45" to 2,
        "14:00" to 3,
        "13:45" to 3,
        "16:00" to 4,
        "15:45" to 4,
        "18:00" to 5,
        "17:45" to 5,
        "20:00" to 6,
        "19:45" to 6,
        "22:00" to 7,
        "21:45" to 7,
    )

    fun parseTimeslot(time: String): Pair<Int, Int> {
        val splitTime = time.split(" - ")
        val start = timeslotStartMap[splitTime[0]] ?: 0
        val length = (timeslotEndMap[splitTime[1]] ?: 0)
        return Pair(start, length)
    }

    val mysteryLink = "https://www.youtube.com/watch?v=nhIQMCXJzLI"
}