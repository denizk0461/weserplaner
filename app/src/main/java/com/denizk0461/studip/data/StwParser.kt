package com.denizk0461.studip.data

import android.util.Log
import com.denizk0461.studip.model.CanteenOffer
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

// Parser class for the Studierendenwerk cafeteria plans
class StwParser {

    private var id = 0

    fun parse(onFinish: () -> Unit) {
        id = 0
        val links = mutableListOf<String>()
        if (true) { links.add(urlUniMensa) }
//        if (true) { links.add(urlCafeCentral) }
//        if (true) { links.add(urlNW1) }
//        if (true) { links.add(urlGW2) }
//        if (true) { links.add(urlHSBNeustadt) }
//        if (true) { links.add(urlHSBAirport) }
//        if (true) { links.add(urlHSBWerder) }
//        if (true) { links.add(urlHfK) }
//        if (true) { links.add(urlMensaBHV) }
//        if (true) { links.add(urlCafeBHV) }

        Dependencies.repo.nukeOffers()

        links.forEach { link ->
            Dependencies.repo.insertOffers(parseFromPage(link))
        }
    }

    private fun parseFromPage(url: String): List<CanteenOffer> {
        val items = mutableListOf<CanteenOffer>()
        var date: String
        var day = 0

        val doc = Jsoup.connect(url).get()
        doc.getElementsByClass("food-plan").forEach { dayPlan ->

            val rawDate = doc.getElementsByClass("tabs")[0]
                .getElementsByClass("tab-date")[day].text().split(" ")

            date = "${rawDate[0]}${rawDate[1].monthToNumber()}."
            day += 1

            dayPlan.getElementsByClass("food-category").forEach { category ->
                val categoryTitle = category.getElementsByClass("category-name")[0].text()
                category
                    .getElementsByTag("tbody")[0]
                    .getElementsByTag("tr").forEach { element ->
                        val tableRows = element.getElementsByTag("td")

                        val prefs =
                            element.getElementsByClass("field field-name-field-food-types")[0]

                        val newItem = CanteenOffer(
                            id = id,
                            date = date,
                            dateId = day,
                            category = categoryTitle,
                            title = tableRows[1].getFilteredText(),
                            price = tableRows.getTextOrEmpty(2),
                            isFair = prefs.isDietaryPreferenceMet(PREFERENCE_FAIR),
                            isFish = prefs.isDietaryPreferenceMet(PREFERENCE_FISH),
                            isPoultry = prefs.isDietaryPreferenceMet(PREFERENCE_POULTRY),
                            isLamb = prefs.isDietaryPreferenceMet(PREFERENCE_LAMB),
                            isVital = prefs.isDietaryPreferenceMet(PREFERENCE_VITAL),
                            isBeef = prefs.isDietaryPreferenceMet(PREFERENCE_BEEF),
                            isPork = prefs.isDietaryPreferenceMet(PREFERENCE_PORK),
                            isVegan = prefs.isDietaryPreferenceMet(PREFERENCE_VEGAN),
                            isVegetarian = prefs.isDietaryPreferenceMet(PREFERENCE_VEGETARIAN),
                            isGame = prefs.isDietaryPreferenceMet(PREFERENCE_GAME),
                        )
                        items.add(newItem)
                        id += 1
                    }
            }
        }

        return items
    }

    private fun Element.isDietaryPreferenceMet(preference: String): Boolean =
        getElementsByAttributeValue("src", preference).isNotEmpty()

    private fun Element.getFilteredText(): String {
        var text = html().replace("&amp;", "&")

        while (text.contains("<sup>")) {
            text = text.substring(0 until text.indexOf("<sup>")) + text.substring(text.indexOf("</sup>")+6 until text.length)
        }

        return text
    }

    private fun Elements.getTextOrEmpty(index: Int): String = try {
        get(index).text()
    } catch (e: java.lang.IndexOutOfBoundsException) {
        ""
    }

    private fun String.monthToNumber(): String = when (this) {
        "Jan" -> "01"
        "Feb" -> "02"
        "Mar" -> "03"
        "Apr" -> "04"
        "Mai" -> "05"
        "Jun" -> "06"
        "Jul" -> "07"
        "Aug" -> "08"
        "Sep" -> "09"
        "Okt" -> "10"
        "Nov" -> "11"
        "Dez" -> "12"
        else -> "00" // shouldn't occur
    }

    private val PREFERENCE_FAIR = "https://www.stw-bremen.de/sites/default/files/images/pictograms/at_small.png"
    private val PREFERENCE_FISH = "https://www.stw-bremen.de/sites/default/files/images/pictograms/fisch.png"
    private val PREFERENCE_POULTRY = "https://www.stw-bremen.de/sites/default/files/images/pictograms/geflugel.png"
    private val PREFERENCE_LAMB = "https://www.stw-bremen.de/sites/default/files/images/pictograms/lamm.png"
    private val PREFERENCE_VITAL = "https://www.stw-bremen.de/sites/default/files/images/pictograms/mensa_vital.png"
    private val PREFERENCE_BEEF = "https://www.stw-bremen.de/sites/default/files/images/pictograms/rindfleisch.png"
    private val PREFERENCE_PORK = "https://www.stw-bremen.de/sites/default/files/images/pictograms/schwein.png"
    private val PREFERENCE_VEGAN = "https://www.stw-bremen.de/sites/default/files/images/pictograms/mensa_vegan.png"
    private val PREFERENCE_VEGETARIAN = "https://www.stw-bremen.de/sites/default/files/images/pictograms/vegetarisch.png"
    private val PREFERENCE_GAME = "https://www.stw-bremen.de/sites/default/files/images/pictograms/wild.png"

    private val urlUniMensa = "https://www.stw-bremen.de/de/mensa/uni-mensa"
    private val urlCafeCentral = "https://www.stw-bremen.de/de/mensa/cafe-central"
    private val urlNW1 = "https://www.stw-bremen.de/de/mensa/nw-1"
    private val urlGW2 = "https://www.stw-bremen.de/de/cafeteria/gw2"
//    private val urlGraz = ""
    private val urlHSBNeustadt = "https://www.stw-bremen.de/de/mensa/neustadtswall"
    private val urlHSBWerder = "https://www.stw-bremen.de/de/mensa/werderstra%C3%9Fe"
    private val urlHSBAirport = "https://www.stw-bremen.de/de/mensa/airport"
    private val urlHfK = "https://www.stw-bremen.de/de/mensa/interimsmensa-hfk"
    private val urlMensaBHV = "https://www.stw-bremen.de/de/mensa/bremerhaven"
    private val urlCafeBHV = "https://www.stw-bremen.de/de/cafeteria/bremerhaven"
}