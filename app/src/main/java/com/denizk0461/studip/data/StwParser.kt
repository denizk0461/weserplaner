package com.denizk0461.studip.data

import com.denizk0461.studip.db.AppRepository
import com.denizk0461.studip.model.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Parser class used for fetching and collecting canteen offers from the website of the
 * Studierendenwerk Bremen.
 */
class StwParser {

    // Unique primary key value for the date elements
    private var dateId = 0

    // Unique primary key value for the canteen elements
    private var canteenId = 0

    // Unique primary key value for the category elements
    private var categoryId = 0

    // Unique primary key value for the item elements
    private var itemId = 0

    // Determine whether there are offers for a given date
    private var dateHasItems = false

    /**
     * Parses through a list of canteen plans and saves them to persistent storage.
     *
     * @param onRefreshUpdate   action call for when an update on the status of the fetch is
     *                          available
     * @param onFinish          action call for when the fetch has finished
     */
    fun parse(canteen: Int, onRefreshUpdate: (status: Int) -> Unit, onFinish: () -> Unit) {
        /*
         * Reset primary key values. This needs to be done in case this method is called multiple
         * times within the app's lifespan
         */
        dateId = 0
        canteenId = 0
        categoryId = 0
        itemId = 0

        // Check which plan needs to be fetched
        val link = when (canteen) {
            0 -> urlUniMensa
            1 -> urlCafeCentral
            2 -> urlNW1
            3 -> urlGW2
            4 -> urlHSBNeustadt
            5 -> urlHSBWerder
            6 -> urlHSBAirport
            7 -> urlMensaBHV
            8 -> urlCafeBHV
            9 -> urlHfK
            else -> urlUniMensa
        }

        // Delete all previous entries and start afresh
        Dependencies.repo.nukeOffers()

        // Fetch offers from the chosen canteen
        parseFromPage(link, Dependencies.repo)

            // TODO implement onRefresh(Int)

        // Action call once all fetching activities have finished
        onFinish()
    }

    /**
     * Fetch and parse the plan of a specific canteen.
     *
     * @param url   link to the canteen to be scraped. Must be a subpage of stw-bremen.de
     * @param repo  reference to the app repository used to save the items to persistent storage
     */
    private fun parseFromPage(url: String, repo: AppRepository) {
        // Used to store a parsed date string without creating a new variable on every loop
        var date: String

        // Reset the primary key value for the date objects
        dateId = 0

        // Parse the HTML using Jsoup to traverse the document
        val doc = Jsoup.connect(url).get()

        /*
         * Retrieve the opening hours for the canteen.
         * TODO this formatting sucks
         */
        var openingHours = ""

        // Iterate through all wrappers that could contain opening hours
        doc.getElementsByClass("details-wrapper").forEach {
            // Element is not for providing contact details
            if (it.getElementsByClass("contact-person-name").size == 0) {

                /*
                 * Retrieve all opening hours.
                 * TODO this slows down the fetch SIGNIFICANTLY, but why?
                 */
                it.children().forEach { element ->
                    if (element.tag().toString() == "p") {
                        openingHours += element.textWithBreaks().trim() + "\n" // Uni-Mensa is not parsed properly
                    } else {
                        element.children().forEach { subElement ->
                            openingHours += subElement.textWithBreaks() + '\n'
                        }
                    }
                }
            }

            // Add an extra line break to separate elements
            openingHours += "\n"
        }

        // Trim off excess line breaks
        openingHours = openingHours.trim()

        // Save the canteen to persistent storage
        repo.insert(
            OfferCanteen(
                canteenId,
                doc.getElementsByClass("pane-title")[1].text(),
                openingHours,
            )
        )

        // Iterate through each day for which offers are available
        doc.getElementsByClass("food-plan").forEach { dayPlan ->

            /*
             * Fetch the date for a given day. It will be in the following format:
             * 24. Apr
             * This value will be split between the day (24.) and the month (Apr)
             */
            val rawDate = doc.getElementsByClass("tabs")[0]
                .getElementsByClass("tab-date")[dateId].text().split(" ")

            /*
             * Parse the two elements into a single date. The month will be converted to a numeric
             * value. Example:
             * Input: 24. Apr
             * Output: 24.04.
             */
            date = "${rawDate[0]}${rawDate[1].monthToNumber()}."

            /*
             * Save the date to persistent storage.
             * TODO this will duplicate date entries for every canteen that is fetched. This is
             *  inefficient.
             */
            repo.insert(OfferDate(dateId, date))

            dateHasItems = false

            // Iterate through all categories offered on a given day
            dayPlan.getElementsByClass("food-category").forEach { category ->

                dateHasItems = true

                // Retrieve the category text
                val categoryTitle = category.getElementsByClass("category-name")[0].text()

                // Save the category to persistent storage
                repo.insert(OfferCategory(categoryId, dateId, canteenId, categoryTitle))

                // Iterate through all items in a category
                category
                    .getElementsByTag("tbody")[0]
                    .getElementsByTag("tr").forEach { element ->
                        // Retrieve the parent element holding the item's title and price
                        val tableRows = element.getElementsByTag("td")

                        // Retrieve the dietary preferences the item meets
                        val prefs = element
                            .getElementsByClass("field field-name-field-food-types")[0]

                        // Parse the preferences into a string for the database
                        val prefString = DietaryPrefObject(
                            isFair = prefs.isDietaryPreferenceMet(imageLinkPrefFair),
                            isFish = prefs.isDietaryPreferenceMet(imageLinkPrefFish),
                            isPoultry = prefs.isDietaryPreferenceMet(imageLinkPrefPoultry),
                            isLamb = prefs.isDietaryPreferenceMet(imageLinkPrefLamb),
                            isVital = prefs.isDietaryPreferenceMet(imageLinkPrefVital),
                            isBeef = prefs.isDietaryPreferenceMet(imageLinkPrefBeef),
                            isPork = prefs.isDietaryPreferenceMet(imageLinkPrefPork),
                            isVegan = prefs.isDietaryPreferenceMet(imageLinkPrefVegan),
                            isVegetarian = prefs.isDietaryPreferenceMet(imageLinkPrefVegetarian),
                            isGame = prefs.isDietaryPreferenceMet(imageLinkPrefGame),
                        ).deconstruct()

                        val filteredText = tableRows[1].getFilteredText()

                        // Save the item to persistent storage
                        repo.insert(
                            OfferItem(
                                itemId,
                                categoryId,
                                title = filteredText.first,
                                price = tableRows.getTextOrEmpty(2),
                                dietaryPreferences = prefString,
                                allergens = filteredText.second,
                            )
                        )

                        // Increment the item ID to avoid conflict
                        itemId += 1
                    }
                // Increment the category ID to avoid conflict
                categoryId += 1
            }

            /*
             * If no items were found for the day, insert items that will tell the user that nothing
             * is available. Text will then be handled in the adapter class.
             */
            if (!dateHasItems) {
                repo.insert(OfferCategory(categoryId, dateId, canteenId, "NO\$ITEMS"))
                repo.insert(
                    OfferItem(
                        itemId,
                        categoryId,
                        title = "NO\$ITEMS",
                        price = "",
                        dietaryPreferences = "..........",
                        allergens = "",
                    )
                )
                // Increment the IDs to avoid conflict
                categoryId += 1
                itemId += 1
            }

            // Increment the date ID to avoid conflict
            dateId += 1
        }
        // Increment the canteen ID to avoid conflict
        canteenId += 1
    }

    /**
     * Evaluates whether a dietary preference is met by checking if the link to an image can be
     * found in the HTML.
     *
     * @param preference    constraint that needs to be met
     * @return              whether it is met
     */
    private fun Element.isDietaryPreferenceMet(preference: String): Boolean =
        getElementsByAttributeValue("src", preference).isNotEmpty()

    /**
     * Processes certain character references into human-readable characters. Since the fetched HTML
     * contains unresolved symbols such as &amp;, they need to be replaced with their counterpart
     * (in this case, &). This method also parses allergens and additives, since they are listed on
     * the website of the Studierendenwerk, but they are invisible, rendering them pointless to the
     * website user.
     *
     * @return  the filtered string and a string describing allergens and additives
     */
    private fun Element.getFilteredText(): Pair<String, String> {
        // Retrieve the element's inner HTML and replace faulty characters
        var text = html()
            .replace("&amp;", "&")
            .replace("&gt;", ">")
            .replace("&lt;", "<")

        /*
         * Allergens are stored in super tags. Example:
         * <sup>4, a1, a4, c, g</sup>
         * This is the start index of the opening tag (inclusive).
         */
        var indexSupOpen: Int

        // This is the end index of the closing tag (exclusive)
        var indexSupClose: Int

        // This list collects the individual items without the <sup> tags and commas
        val allergens = mutableListOf<String>()

        /*
         * The Studierendenwerk's website lists allergens, but they are invisible, rendering them
         * pointless to the website user. As of now, this information is discarded in the app.
         * TODO implement allergen functionality
         */
        while (text.contains("<sup>")) {

            // Set indices for the <sup> tags
            indexSupOpen = text.indexOf("<sup>")
            indexSupClose = text.indexOf("</sup>")

            // Add allergens
            allergens.addAll(
                text.substring(indexSupOpen + 5 until indexSupClose)
                    // Remove redundant whitespace
                    .replace(" ", "")
                    /*
                     * Split individual strings with multiple allergens so that duplicates can be
                     * filtered out later.
                     */
                    .split(",")
            )

            // Remove allergens from the title of the offer
            text = text.substring(0 until indexSupOpen) +
                    text.substring((indexSupClose + 6) until text.length)
        }

        /*
         * Return the stripped and updated HTML string and join the allergen list to a cohesive
         * string without duplication.
         */
        return Pair(text, allergens.distinct().joinToString(","))
    }

    /**
     * Retrieve the text of an element in a certain position if the element can be found. Else,
     * return an empty string.
     *
     * @param index position at which a child
     * @return      text content or, if no element is found, an empty string
     */
    private fun Elements.getTextOrEmpty(index: Int): String = try {
        get(index).text()
    } catch (e: java.lang.IndexOutOfBoundsException) {
        ""
    }

    /**
     * Converts a month text string to its numeric value. Example:
     * Input: Apr
     * Output: 04
     *
     * @return  the numeric value of the month
     */
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

    /**
     * Retrieves a text and adds <br> tags with line breaks.
     *
     * @return  formatted element text
     */
    private fun Element.textWithBreaks(): String =
        this.text()//html()
//            .replace("<br>", "\n")
//            .replace("&nbsp;", " ")
//            .replace("<strong>", "")
//            .replace("</strong>", "")
//            .replace("<p>", "")
//            .replace("</p>", "")

    // Image links to all dietary preferences used for checking whether a preference is met
    private val imageLinkPrefFair = "https://www.stw-bremen.de/sites/default/files/images/pictograms/at_small.png"
    private val imageLinkPrefFish = "https://www.stw-bremen.de/sites/default/files/images/pictograms/fisch.png"
    private val imageLinkPrefPoultry = "https://www.stw-bremen.de/sites/default/files/images/pictograms/geflugel.png"
    private val imageLinkPrefLamb = "https://www.stw-bremen.de/sites/default/files/images/pictograms/lamm.png"
    private val imageLinkPrefVital = "https://www.stw-bremen.de/sites/default/files/images/pictograms/mensa_vital.png"
    private val imageLinkPrefBeef = "https://www.stw-bremen.de/sites/default/files/images/pictograms/rindfleisch.png"
    private val imageLinkPrefPork = "https://www.stw-bremen.de/sites/default/files/images/pictograms/schwein.png"
    private val imageLinkPrefVegan = "https://www.stw-bremen.de/sites/default/files/images/pictograms/mensa_vegan.png"
    private val imageLinkPrefVegetarian = "https://www.stw-bremen.de/sites/default/files/images/pictograms/vegetarisch.png"
    private val imageLinkPrefGame = "https://www.stw-bremen.de/sites/default/files/images/pictograms/wild.png"

    // URLs to all canteens in Bremen and Bremerhaven managed by the Studierendenwerk Bremen
    private val urlUniMensa = "https://www.stw-bremen.de/de/mensa/uni-mensa"
    private val urlCafeCentral = "https://www.stw-bremen.de/de/mensa/cafe-central"
    private val urlNW1 = "https://www.stw-bremen.de/de/mensa/nw-1"
    private val urlGW2 = "https://www.stw-bremen.de/de/cafeteria/gw2"
//    private val urlGraz = "" // No link since there are only snacks on offer that are not listed online
    private val urlHSBNeustadt = "https://www.stw-bremen.de/de/mensa/neustadtswall"
    private val urlHSBWerder = "https://www.stw-bremen.de/de/mensa/werderstra%C3%9Fe"
    private val urlHSBAirport = "https://www.stw-bremen.de/de/mensa/airport"
    private val urlHfK = "https://www.stw-bremen.de/de/mensa/interimsmensa-hfk"
    private val urlMensaBHV = "https://www.stw-bremen.de/de/mensa/bremerhaven"
    private val urlCafeBHV = "https://www.stw-bremen.de/de/cafeteria/bremerhaven"
}