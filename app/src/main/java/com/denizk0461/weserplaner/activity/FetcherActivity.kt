package com.denizk0461.weserplaner.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.denizk0461.weserplaner.R
import com.denizk0461.weserplaner.data.showErrorSnackBar
import com.denizk0461.weserplaner.data.showToast
import com.denizk0461.weserplaner.databinding.ActivityFetcherBinding
import com.denizk0461.weserplaner.model.TextSheetContentId
import com.denizk0461.weserplaner.sheet.TextSheet
import com.denizk0461.weserplaner.viewmodel.FetcherViewModel
import java.io.IOException
import java.net.URLDecoder

/**
 * Activity for allowing the user to log in to access and fetch their schedule from their Stud.IP
 * profile. Launched through a button in the app's settings.
 */
class FetcherActivity : FragmentActivity() {

    // View binding
    private lateinit var binding: ActivityFetcherBinding

    // View model
    private lateinit var viewModel: FetcherViewModel

    // URL of the schedule
    private val scheduleUrl: String = "https://elearning.uni-bremen.de/dispatch.php/calendar/schedule"

    /**
     * Enabling JavaScript is necessary to get the HTML source in this context. As the HTML can only
     * be accessed once the user has logged in, a simple HTML request would not suffice here.
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate view model
        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(FetcherViewModel::class.java)

        // Inflate view binding and bind to this activity
        binding = ActivityFetcherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Remind the user of the help button
        showToast(this, getString(R.string.toast_info_reminder))

        // Enabling JavaScript. See comment above lint suppression for reason why this is done.
        binding.webview.settings.apply {
            javaScriptEnabled = true
        }

        binding.webview.webViewClient = object : WebViewClient() {
            // Disallow redirecting to prevent the app from launching Chrome after the user logs in
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                // Check whether the button to save the timetable should be shown
                if (url?.contains(scheduleUrl) == true) {
                    binding.fab.visibility = View.VISIBLE
                } else {
                    binding.fab.visibility = View.GONE
                }

                // Super call
                super.doUpdateVisitedHistory(view, url, isReload)
            }
        }

        // Set up bottom app bar for various actions
        binding.bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.close -> {
                    // Close the activity
                    finish()
                    true
                }
                R.id.refresh -> {
                    // Refresh the currently opened page
                    binding.webview.reload()
                    true
                }
                R.id.help -> {
                    // Present the user with a small tutorial on how to fetch their timetable
                    TextSheet().also { sheet ->
                        val bundle = Bundle()
                        bundle.putString("header", getString(R.string.fetch_bar_help_sheet_title))
                        bundle.putString("content", getString(R.string.fetch_bar_help_sheet_content))
                        bundle.putBoolean("isCancellable", true)
                        bundle.putInt("contentId", TextSheetContentId.PASS_RAW_STRING)
                        sheet.arguments = bundle
                    }.show(supportFragmentManager, TextSheet::class.java.simpleName)
                    true
                }
                else -> false
            }
        }

        /*
         * Attempt to load the user-set Stud.IP front page. Redirects to login page if user is
         * not logged in.
         */
        binding.webview.loadUrl("https://elearning.uni-bremen.de/index.php?again=yes")

        binding.fab.setOnClickListener {

            if (binding.webview.url?.contains(
                    scheduleUrl
                ) == true) {
                /*
                 * Retrieve encoded HTML via JavaScript function. Encoding is done as otherwise not all
                 * symbols will be accurately fetched.
                 */
                binding.webview.evaluateJavascript(
                    "(function(){return encodeURI(document.getElementsByTagName('html')[0].innerHTML)})();"
                ) { p0 ->
                    try {
                        // Decode HTML and parse it into a list of StudIPEvent.kt
                        val elementsNotFetched = viewModel.parse(URLDecoder.decode(p0, "UTF-8"))

                        /*
                         * Notify the user that the fetch was successful, and tell the user how many
                         * items could not be fetched.
                         */
                        showToast(this, when (elementsNotFetched) {
                            0 -> getString(R.string.toast_fetch_finished_zero)
                            1 -> getString(R.string.toast_fetch_finished_one)
                            else -> getString(R.string.toast_fetch_finished_other, elementsNotFetched)
                        })

                        // Close the activity
                        finish()

                    } catch (e: IOException) {
                        // Let the user know that an error occurred
                        theme.showErrorSnackBar(binding.rootView, getString(R.string.fetch_error_snack), binding.bottomAppBar)
                    }
                }
            } else {
                // Tell the user that they must navigate to their timetable
                theme.showErrorSnackBar(binding.rootView, getString(R.string.fetch_error_webpage_snack), binding.bottomAppBar)
            }
        }
    }
}