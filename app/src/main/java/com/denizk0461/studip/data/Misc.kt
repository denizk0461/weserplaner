package com.denizk0461.studip.data

import com.denizk0461.studip.R

object Misc {

    private val timeslotAcademicQuarter = mapOf(
        "6:00" to "6:15",
        "8:00" to "7:45",
        "8:00" to "8:15",
        "10:00" to "9:45",
        "10:00" to "10:15",
        "12:00" to "11:45",
        "12:00" to "12:15",
        "14:00" to "13:45",
        "14:00" to "14:15",
        "16:00" to "15:45",
        "16:00" to "16:15",
        "18:00" to "17:45",
        "18:00" to "18:15",
        "20:00" to "19:45",
        "20:00" to "20:15",
        "22:00" to "21:45",
    )

    val indexToDrawable: Map<Int, Int> = mapOf(
        0 to R.drawable.handshake,
        1 to R.drawable.fish,
        2 to R.drawable.chicken,
        3 to R.drawable.sheep,
        4 to R.drawable.yoga,
        5 to R.drawable.cow,
        6 to R.drawable.pig,
        7 to R.drawable.leaf,
        8 to R.drawable.carrot,
        9 to R.drawable.deer,
        10 to R.drawable.circle,
    )

    const val mysteryLink = "https://www.youtube.com/watch?v=nhIQMCXJzLI"
}