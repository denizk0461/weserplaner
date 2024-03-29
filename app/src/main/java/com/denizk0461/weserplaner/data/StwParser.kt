package com.denizk0461.weserplaner.data

import android.app.Application
import com.denizk0461.weserplaner.db.AppRepository
import com.denizk0461.weserplaner.values.DietaryPreferences
import com.denizk0461.weserplaner.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import kotlin.jvm.Throws

/**
 * Parser class used for fetching and collecting canteen offers from the website of the
 * Studierendenwerk Bremen.
 */
class StwParser(application: Application) {

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
     * Static instance of the app's repository.
     */
    private val repo: AppRepository = AppRepository.getRepositoryInstance(application)

    /**
     * Parses through a list of canteen plans and saves them to persistent storage.
     *
     * @param onFinish  action call for when the fetch has finished
     * @param onError   action call for when an error has occurred
     */
    suspend fun parse(
        canteen: Int,
        onFinish: () -> Unit,
        onError: () -> Unit
    ) {
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

        /*
         * Handle the fetch operation in an encompassing try-catch to prevent the app from crashing.
         * User will be notified if an error occurs.
         */
        try {

            // Fetch offers from the chosen canteen
            val (dates, canteens, categories, items) = parseFromPage(
                "https://www.stw-bremen.de/de/$link"
            )

            // Delete all previous entries and start afresh
            repo.nukeOffers()

            // Save everything all at once into the database
            with(repo) {
                insertDates(dates)
                insertCanteens(canteens)
                insertCategories(categories)
                insertItems(items)
            }

            /*
             * Call action to let the user know the fetch has finished. Do this on the main thread
             * to access UI.
             */
            withContext(Dispatchers.Main) {
                onFinish()
            }

        } catch (e: RuntimeException) {
            /*
             * Call action to let the user know an error occurred. Do this on the main thread to
             * access UI.
             */
            withContext(Dispatchers.Main) {
                onError()
            }
        }
    }

    /**
     * Fetch and parse the plan of a specific canteen.
     *
     * @param url               link to the canteen to be scraped. Must be a subpage of stw-bremen.de
     * @throws RuntimeException if something goes wrong during the fetch
     */
    @Throws(RuntimeException::class)
    private fun parseFromPage(url: String): StwResults {

        val dates = mutableListOf<OfferDate>()
        val canteens = mutableListOf<OfferCanteen>()
        val categories = mutableListOf<OfferCategory>()
        val items = mutableListOf<OfferItem>()

        // Reset the primary key value for the date objects
        dateId = 0

        // Parse the HTML using Jsoup to traverse the document
        val doc = Jsoup.connect(url).get()

        // Retrieve the opening hours for the canteen
        val openingHours = doc.retrieveOpeningHours()

        // Fetch news displayed at the top of the page
        val news = doc.retrieveNews()

        // Save the canteen to its list
        canteens.add(
            OfferCanteen(
                canteenId,
                doc.getElementsByClass("pane-title")[1].text(),
                openingHours,
                news,
            )
        )

        // Iterate through each day for which offers are available
        doc.getElementsByClass("food-plan").forEach { dayPlan ->

            // Get and add the date of this food plan
            dates.add(doc.getDateByIndex(dateId))

            // Set that the newly created date has no items as of yet
            dateHasItems = false

            // Iterate through all categories offered on a given day
            dayPlan.getElementsByClass("food-category").forEach { category ->

                // Set that items for the newly created date have been found
                dateHasItems = true

                // Retrieve the category text
                val categoryTitle = category.getElementsByClass("category-name")[0].text()

                // Save the category to its list
                categories.add(OfferCategory(categoryId, dateId, canteenId, categoryTitle))

                // Iterate through all items in a category
                category.getAllFoodItems().forEach { element ->
                    // Retrieve the parent element holding the item's title and price
                    val tableRows = element.getElementsByTag("td")

                    // Retrieve the dietary preferences the item meets
                    val prefs = getOrElse(DietaryPreferences.NONE_MET) {
                        element
                            .getElementsByClass("field field-name-field-food-types")[0]
                            .getDietaryPreferencesObject()
                    }

                    // Retrieve title and embedded allergens individually
                    val filteredText = getOrElse(orElse = Pair("", "")) {
                        tableRows[1].splitTitleAndAllergens()
                    }

                    // Save the item to its list
                    items.add(OfferItem(
                        itemId,
                        categoryId,
                        title = filteredText.first,
                        price = tableRows.getTextOrEmpty(
                            /*
                             * ID stored for student: 0
                             * Index required to fetch student price: 2
                             *
                             * ID stored for employee: 1
                             * Index required to fetch employee price: 3
                             *
                             * Hence, getIntPreference() + 2
                             */
                            repo.getPreferencePricing() + 2
                        ),
                        dietaryPreferences = prefs.deconstruct(),
                        allergens = filteredText.second,
                    ))

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
                categories.add(OfferCategory(categoryId, dateId, canteenId, "NO\$ITEMS"))
                items.add(
                    OfferItem(
                        itemId,
                        categoryId,
                        title = "NO\$ITEMS",
                        price = "",
                        dietaryPreferences = "tttttttttt",
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

        return StwResults(dates, canteens, categories, items)
    }

    /**
     * Retrieves a date object from the canteen website by its index.
     *
     * @receiver    document to parse
     * @param index position to retrieve date from
     * @return      date object
     */
    private fun Document.getDateByIndex(index: Int): OfferDate {
        val day = getElementsByClass("tabs")[0]
            .getElementsByClass("tab-title")[index].text()

        /*
         * Fetch the date for a given day. It will be in the following format:
         * 24. Apr
         * This value will be split between the day (24.) and the month (Apr)
         */
        val rawDate = getElementsByClass("tabs")[0]
            .getElementsByClass("tab-date")[index].text().split(" ")


        /*
         * Parse the two elements into a single date. The month will be converted to a numeric
         * value. Example:
         * Input: 24. Apr
         * Output: 24.04.
         */
        val date = "${rawDate[0]}${rawDate[1].monthToNumber()}."

        // Save the date to its list
        return OfferDate(dateId, day, date)
    }

    /**
     * Retrieves all food items from a given element for a given day.
     *
     * @receiver    element to retrieve food item elements from
     * @return      list of food elements
     */
    private fun Element.getAllFoodItems(): Elements =
        getElementsByTag("tbody")[0].getElementsByTag("tr")

    /**
     * Evaluates whether a dietary preference is met by checking if the link to an image can be
     * found in the HTML.
     *
     * @receiver            element to check the image source of
     * @param preference    constraint that needs to be met
     * @return              whether it is met
     */
    private fun Element.isDietaryPreferenceMet(preference: String): Boolean =
        getElementsByAttributeValue(
            "src",
            "https://www.stw-bremen.de/sites/default/files/images/pictograms/$preference.png"
        ).isNotEmpty()

    /**
     * Retrieves a dietary preferences object from a given Element.
     *
     * @receiver    element to retrieve preferences from
     * @return      [DietaryPreferences.Object]
     */
    private fun Element.getDietaryPreferencesObject(): DietaryPreferences.Object =
        DietaryPreferences.Object(
            isFair = isDietaryPreferenceMet(preferenceFairFileName),
            isFish = isDietaryPreferenceMet(preferenceFishFileName),
            isPoultry = isDietaryPreferenceMet(preferencePoultryFileName),
            isLamb = isDietaryPreferenceMet(preferenceLambFileName),
            isVital = isDietaryPreferenceMet(preferenceVitalFileName),
            isBeef = isDietaryPreferenceMet(preferenceBeefFileName),
            isPork = isDietaryPreferenceMet(preferencePorkFileName),
            isVegan = isDietaryPreferenceMet(preferenceVeganFileName),
            isVegetarian = isDietaryPreferenceMet(preferenceVegetarianFileName),
            isGame = isDietaryPreferenceMet(preferenceGameFileName),
        )

    /**
     * Retrieves the item's title and its dietary preferences that are embedded in its title.
     * Processes certain character references into human-readable characters. Since the fetched HTML
     * contains unresolved symbols such as &amp;, they need to be replaced with their counterpart
     * (in this case, &). This method also parses allergens and additives, since they are listed on
     * the website of the Studierendenwerk, but they are invisible, rendering them pointless to the
     * website user.
     *
     * @receiver    element to parse the text content of
     * @return      the filtered title and a string describing allergens and additives
     */
    private fun Element.splitTitleAndAllergens(): Pair<String, String> {
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
     * @receiver    list of elements where a specific element should be retrieved from
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
     * @receiver    month as 3-character string
     * @return      the numeric value of the month
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
     * Retrieve a given element T through a certain action, or else retrieve a safe object, if the
     * action throws an exception.
     *
     * @param   orElse  return value if action throws
     * @param   action  action to try to retrieve value T
     * @return          a value T guaranteed to be returned without throwing an exception
     */
    private fun <T> getOrElse(orElse: T, action: () -> T): T = try {
        action()
    } catch (e: Exception) {
        orElse
    }

    /**
     * Cleans up a HTML source by doing the following:
     * - replace non-breaking spaces '&nbsp;' with a regular space character ' ',
     * - replace closing paragraph tags '</p>' with a line break '\n', and
     * - remove any other tag that does not start with 'a' or 'b' – this is a hacky way to preserve
     * <a> and <br> tags.
     * This is used to format the opening hours of the canteens.
     *
     * @receiver    Element to parse the text of
     * @return      beautified text
     */
    private fun Element.cleanHtml(): String {
        // Replace non-breaking spaces with a regular space character
        var result: String = outerHtml().replace("&nbsp;", " ")

        // Replace paragraph closing tags with a newline character
        result = result.replace("</p>", "\n")

        // Remove all other characters
        // 'a' is excluded to avoid removing the <a> tags
        return result.replace(Regex("</?[c-zA-Z0-9]+>"), "")
    }

    /**
     * Retrieves the opening hours of a canteen from a given document.
     *
     * @receiver    document to parse
     * @return      opening hours parsed as a string
     */
    private fun Document.retrieveOpeningHours(): String {
        // Retrieve the opening hours for the canteen
        var openingHours = ""

        // Iterate through all containers that may contain opening hours
        getElementsByClass("nm").forEach { container ->
            // Attempt to fetch a header, if any is available
            try {
                // Check for a header in the parent element of the container
                val header = container.parent()?.getElementsByClass("strong")?.get(0)?.text()

                // Check if the header has content for the opening hours
                if (!header.isNullOrBlank()
                    && !header.contains("Frau")
                    && !header.contains("Herr")
                ) {
                    // Add the header to the opening hours
                    openingHours += "${header}\n"
                }
            } catch (e: IndexOutOfBoundsException) {
                // Ignore the exception; no header is available, and thus, nothing needs to be done
            }

            // Iterate through all items in the container
            container.children().forEach { element ->
                // Clean up HTML
                val line = element.cleanHtml()

                // Confirm that this line only contains information for the opening hours
                if (!line.contains("Catering")) {
                    // Add the line to the opening hours
                    openingHours += "${line}<br>"
                }
            }
        }

        // Trim off excess line breaks
        openingHours = openingHours.trim()

        // Return the result
        return openingHours
    }

    /**
     * Retrieves news available at the top of the page in a special header. If none are available,
     * return a blank string.
     *
     * @receiver    document to parse
     * @return      news parsed as a string
     */
    private fun Document.retrieveNews(): String = try {
        getElementsByClass("field__items")[0]
            .getElementsByTag("p")[0]
            .html()
    } catch (e: IndexOutOfBoundsException) {
        ""
    }

    /**
     * Wrapper tuple class to transfer all fetched elements in one go.
     *
     * @param dates         fetched date elements
     * @param canteens      fetched canteen elements
     * @param categories    fetched category elements
     * @param items         fetched items
     */
    private data class StwResults(
        val dates: List<OfferDate>,
        val canteens: List<OfferCanteen>,
        val categories: List<OfferCategory>,
        val items: List<OfferItem>,
    )

    /*
     * File names for all dietary preferences on the Stw website used for checking whether a
     * preference is met.
     */
    private val preferenceFairFileName = "at_small"
    private val preferenceFishFileName = "fisch"
    private val preferencePoultryFileName = "geflugel"
    private val preferenceLambFileName = "lamm"
    private val preferenceVitalFileName = "mensa_vital"
    private val preferenceBeefFileName = "rindfleisch"
    private val preferencePorkFileName = "schwein"
    private val preferenceVeganFileName = "mensa_vegan"
    private val preferenceVegetarianFileName = "vegetarisch"
    private val preferenceGameFileName = "wild"

    /*
     * URL artifacts linking to all canteens in Bremen and Bremerhaven managed by the
     * Studierendenwerk Bremen.
     */
    private val urlUniMensa = "mensa/uni-mensa"
    private val urlCafeCentral = "mensa/cafe-central"
    private val urlNW1 = "mensa/nw-1"
    private val urlGW2 = "cafeteria/gw2"
    private val urlHSBNeustadt = "mensa/neustadtswall"
    private val urlHSBWerder = "mensa/werderstraße"
    private val urlHSBAirport = "mensa/airport"
    private val urlHfK = "mensa/interimsmensa-hfk"
    private val urlMensaBHV = "mensa/bremerhaven"
    private val urlCafeBHV = "cafeteria/bremerhaven"
//    private val urlGraz = "" // No link since there are only snacks on offer that are not listed online
}