package com.denizk0461.studip.data

object DummyData {

    val events: Array<StudIPEvent> = arrayOf(
        StudIPEvent("Literatures", arrayOf("Nittel"), "MZH 1380/1400", 0, 3, 1),
        StudIPEvent("Linguistics", arrayOf("Du Bois"), "SFG 1020", 1, 1, 1),
        StudIPEvent("System Erde", arrayOf("Zolitschka", "Labuhn-Deroubaix"), "GW2 B1410", 1, 3, 1),
        StudIPEvent("Key Moments", arrayOf("Esders-Angermund"), "GW2 B2890", 2, 2, 1),
        StudIPEvent("ULS-1", arrayOf("Herrmann"), "GW2 B2890", 2, 3, 1),
        StudIPEvent("Gesellschaft und Raum", arrayOf("Lossau", "Mossig"), "GW2 B1820", 3, 1, 1),
        StudIPEvent("Gesellschaft und Raum2", arrayOf("Lossau", "Mossig"), "GW2 B1820", 4, 4, 2),
    )
}