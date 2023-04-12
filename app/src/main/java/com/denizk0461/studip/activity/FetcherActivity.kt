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
import com.denizk0461.studip.databinding.ActivityFetcherBinding
import com.denizk0461.studip.viewmodel.FetcherViewModel
import java.net.URLDecoder

class FetcherActivity : Activity() {

    private lateinit var binding: ActivityFetcherBinding
    private lateinit var viewModel: FetcherViewModel

    /*
     * Enabling JavaScript is necessary to get the HTML source in this context. As the HTML can only
     * be accessed once the user has logged in, a simple HTML request would not suffice here.
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider.AndroidViewModelFactory(this.application).create(FetcherViewModel::class.java)

        binding = ActivityFetcherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webview.settings.apply {
            javaScriptEnabled = true
        }
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }
            override fun onPageFinished(view: WebView, url: String) {
                binding.webview.loadUrl(
                    "javascript:window.HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"
                )
            }
        }

        binding.webview.loadUrl("https://elearning.uni-bremen.de/index.php?again=yes")

        binding.fab.setOnClickListener { view ->
            binding.webview.evaluateJavascript(
                "(function(){return encodeURI(document.getElementsByTagName('html')[0].innerHTML)})();"
            ) { p0 ->
                viewModel.nukeEvents()
                StudIPParser().parse(URLDecoder.decode(p0, "UTF-8")) { events ->
                    viewModel.insertEvents(events)
                    Toast.makeText(this, R.string.toast_fetch_finished, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}