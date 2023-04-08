package com.denizk0461.studip.data

import com.denizk0461.studip.model.StudIPEvent

object DummyData {

    val events: Array<StudIPEvent> = arrayOf(
        StudIPEvent(1, "Literatures", "Nittel", "MZH 1380/1400", 0, 3, 1),
        StudIPEvent(2, "Linguistics", "Du Bois", "SFG 1020", 1, 1, 1),
        StudIPEvent(3, "System Erde", "Zolitschka, Labuhn-Deroubaix", "GW2 B1410", 1, 3, 1),
        StudIPEvent(4, "Key Moments", "Esders-Angermund", "GW2 B2890", 2, 2, 1),
        StudIPEvent(5, "ULS-1", "Herrmann", "GW2 B2890", 2, 3, 1),
        StudIPEvent(6, "Gesellschaft und Raum", "Lossau, Mossig", "GW2 B1820", 3, 1, 1),
        StudIPEvent(7, "Gesellschaft und Raum2", "Lossau, Mossig", "GW2 B1820", 4, 4, 2),
    )
}