package com.denizk0461.studip.sheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.denizk0461.studip.R
import com.denizk0461.studip.data.viewBinding
import com.denizk0461.studip.databinding.SheetDevCodeBinding
import java.nio.charset.StandardCharsets

/**
 * Sheet that presents the user / developer with a text field to input developer codes / cheat
 * codes.
 *
 * @param nukeEvents            deletes all events
 * @param nukeOfferItems        deletes all canteen items
 * @param nukeOfferCategories   deletes all canteen categories
 * @param nukeOfferCanteens     deletes all canteens
 * @param nukeOfferDates        deletes all canteen dates
 * @param nukeEverything        deletes everything
 */
class DevCodeSheet(
    private val nukeEvents: () -> Unit,
    private val nukeOfferItems: () -> Unit,
    private val nukeOfferCategories: () -> Unit,
    private val nukeOfferCanteens: () -> Unit,
    private val nukeOfferDates: () -> Unit,
    private val nukeEverything: () -> Unit,
) : AppSheet(R.layout.sheet_dev_code) {

    // View binding
    private val binding: SheetDevCodeBinding by viewBinding(SheetDevCodeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Execute an action based on the input
        binding.buttonConfirm.setOnClickListener {
            /*
             * Codes are implemented here.
             *
             * About the inks; they are safe (which is suspicious of me to say), just encoded so
             * people can't just look at the file and figure out all the codes! Of course, they're
             * easily decoded, but who would go for that? You would have to be a pretty big nerd to
             * do that. Then again, I was the one who encoded all of these links in the first place,
             * so I suppose the joke's on me...
             *
             * If you're reading this – why?
             */
            when (binding.input.text.toString().uppercase()) {
                "U1RST0JF".d() -> launchLink("aHR0cHM6Ly95b3V0dS5iZS90S2k5Wi1mNnFYNA==".d())
                "U1BJUklU".d() -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9XN25lMzlEU05TZw==".d())
                "Q0xPVURT".d() -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9rbUJ6YTRhNTB2dw==".d())
                "NjQxODJL".d() -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9EbTFyZ285UHBUcw==".d())
                "U0FJTE9S".d() -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9QUHlDYXZ6OG5NWQ==".d())
                "SE9ORVNU".d() -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9DSEg0OE5LUEFraw==".d())
                "QkJVT0s/".d() -> launchLink("aHR0cHM6Ly95b3V0dS5iZS90Rm1PMm1TY0tHNA==".d())
                "NEVENT" -> { // nuke events
                    nukeEvents()
                    showToast("Nuked all events")
                }
                "NCANTE" -> { // nuke canteens
                    nukeOfferCanteens()
                    showToast("Nuked all canteens")
                }
                "NDATES" -> { // nuke dates
                    nukeOfferDates()
                    showToast("Nuked all dates")
                }
                "NCATEG" -> { // nuke categories
                    nukeOfferCategories()
                    showToast("Nuked all categories")
                }
                "NITEMS" -> { // nuke items
                    nukeOfferItems()
                    showToast("Nuked all items")
                }
                "NEVERY" -> { // nuke everything
                    nukeEverything()
                    showToast("Nuked everything")
                }
                else -> showToast("code is invalid")
            }
            dismiss()
        }
    }

    /**
     * Present the user with a toast.
     *
     * @param text  text to display
     */
    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * Launches a web link.
     *
     * @param link  link to open
     */
    private fun launchLink(link: String) {
        // Let the user know they accomplished something with their life
        showToast("✨")

        // Give the user a slight dopamine boost
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    /**
     * Encodes a string to Base64.
     *
     * @return the encoded string
     */
    private fun String.e(): String = Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)


    /**
     * Decodes Base64 to a regular string.
     *
     * @return the decoded string
     */
    private fun String.d(): String = String(
        Base64.decode(this, Base64.DEFAULT),
        StandardCharsets.UTF_8
    )
}