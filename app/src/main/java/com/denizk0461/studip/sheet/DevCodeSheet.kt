package com.denizk0461.studip.sheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import com.denizk0461.studip.R
import com.denizk0461.studip.activity.ImageActivity
import com.denizk0461.studip.data.showToast
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
             * easily decoded, but who would go for that? You would have to have a lot of time to
             * waste to do that. Then again, I was the one who encoded all of these links in the
             * first place, so I suppose the joke's on me...
             *
             * If you're reading this, I ask you – why?
             */
            when (binding.input.text.toString().uppercase()) {
                "U1RST0JF".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS90S2k5Wi1mNnFYNA==".d64)
                "U1BJUklU".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9XN25lMzlEU05TZw==".d64)
                "Q0xPVURT".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9rbUJ6YTRhNTB2dw==".d64)
                "NjQxODJL".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9EbTFyZ285UHBUcw==".d64)
                "U0FJTE9S".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9QUHlDYXZ6OG5NWQ==".d64)
                "SE9ORVNU".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9DSEg0OE5LUEFraw==".d64)
                "QkJVT0s/".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS90Rm1PMm1TY0tHNA==".d64)
                "SUhUU0Mq".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS85bXZ4SVdhWHZuWQ==".d64)
                "SU5TQU5F".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS90NE9kYTlUZFYwbw==".d64)
                "X0RSV05f".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9SUVpUUy1CWjhtcw==".d64)
                "TUFSR0Uh".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS8tbHFxRHZXRjQ1dw==".d64)
                "UE9XRUxM".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9XUGMtVkVxQlBISQ==".d64)
                "Q0FMTE1F".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS8xbTV1WnJNMnktMA==".d64)
                "SkFNSVJP".d64 -> launchLink("aHR0cHM6Ly95b3V0dS5iZS9fMVZoT2c0N3ozNA==".d64)
                "Qk9KQUNL".d64 -> {
                    startActivity(Intent(context, ImageActivity::class.java).also { intent ->
                        val bundle = Bundle()
                        bundle.putString("img", "bojack")
                        intent.putExtras(bundle)
                    })
                }
                "QkNFTExT".d64 -> {
                    startActivity(Intent(context, ImageActivity::class.java).also { intent ->
                        val bundle = Bundle()
                        bundle.putString("img", "bcells")
                        intent.putExtras(bundle)
                    })
                }
                "NEVENT" -> { // nuke events
                    nukeEvents()
                    showToast(context, "Nuked all events")
                }
                "NCANTE" -> { // nuke canteens
                    nukeOfferCanteens()
                    showToast(context, "Nuked all canteens")
                }
                "NDATES" -> { // nuke dates
                    nukeOfferDates()
                    showToast(context, "Nuked all dates")
                }
                "NCATEG" -> { // nuke categories
                    nukeOfferCategories()
                    showToast(context, "Nuked all categories")
                }
                "NITEMS" -> { // nuke items
                    nukeOfferItems()
                    showToast(context, "Nuked all items")
                }
                "NEVERY" -> { // nuke everything
                    nukeEverything()
                    showToast(context, "Nuked everything")
                }
                else -> showToast(context, "Code is invalid")
            }
            dismiss()
        }
    }

    /**
     * Launches a web link.
     *
     * @param link  link to open
     */
    private fun launchLink(link: String) {
        // Let the user know they accomplished something with their life
        showToast(context, "✨")

        // Give the user a slight dopamine boost
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    /**
     * Encodes a string to Base64.
     *
     * @return the encoded string
     */
    private val String.e64: String get() = Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)


    /**
     * Decodes Base64 to a regular string.
     *
     * @return the decoded string
     */
    private val String.d64: String get() = String(
        Base64.decode(this, Base64.DEFAULT),
        StandardCharsets.UTF_8
    )
}