package com.denizk0461.studip.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.denizk0461.studip.R
import com.denizk0461.studip.data.StudIPParser
import com.denizk0461.studip.data.getThemedColor
import com.denizk0461.studip.databinding.ActivityFetcherBinding
import com.denizk0461.studip.viewmodel.FetcherViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.net.URLDecoder

/**
 * Activity for allowing the user to log in to access and fetch their schedule from their Stud.IP
 * profile. Launched through a button in the app's settings.
 */
class FetcherActivity : Activity() {

    // View binding
    private lateinit var binding: ActivityFetcherBinding
    // View model
    private lateinit var viewModel: FetcherViewModel

    /**
     * Enabling JavaScript is necessary to get the HTML source in this context. As the HTML can only
     * be accessed once the user has logged in, a simple HTML request would not suffice here.
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate view model
        viewModel = ViewModelProvider.AndroidViewModelFactory(this.application).create(FetcherViewModel::class.java)
        // Inflate view binding and bind to this activity
        binding = ActivityFetcherBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        }

        /*
         * Attempt to load the user-set Stud.IP front page. Redirects to login page if user is
         * not logged in.
         */
        binding.webview.loadUrl("https://elearning.uni-bremen.de/index.php?again=yes")

        binding.fab.setOnClickListener { view ->
            /*
             * Retrieve encoded HTML via JavaScript function. Encoding is done as otherwise not all
             * symbols will be accurately fetched.
             */
            binding.webview.evaluateJavascript(
                "(function(){return encodeURI(document.getElementsByTagName('html')[0].innerHTML)})();"
            ) { p0 ->
                try {
                    // Decode HTML and parse it into a list of StudIPEvent.kt
                    StudIPParser().parse(URLDecoder.decode(p0, "UTF-8")) { events ->
                        // Delete all previously fetched elements
                        viewModel.nukeEvents()
                        // Insert the list into the database
                        viewModel.insertEvents(events)
                        // Notify the user that the fetch was successful
                        Toast.makeText(
                            this, R.string.toast_fetch_finished, Toast.LENGTH_SHORT
                        ).show()
                        // Close the activity
                        finish()
                    }
                } catch (e: IOException) {

                    // Let the user know that an error occurred
                    Snackbar
                        .make(
                            binding.rootView,
                            getString(R.string.fetch_error_snack),
                            Snackbar.LENGTH_SHORT
                        )
                        // Set colours to signify an error
                        .setBackgroundTint(theme.getThemedColor(R.attr.colorErrorContainer))
                        .setTextColor(theme.getThemedColor(R.attr.colorOnErrorContainer))
                        .show()
                }
            }
        }
    }
}