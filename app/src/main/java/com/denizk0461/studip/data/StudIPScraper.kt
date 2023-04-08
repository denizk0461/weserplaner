package com.denizk0461.studip.data

import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

class StudIPScraper : WebViewClient() {

    fun parse(context: Context) {

//        val res = Jsoup.connect("https://elearning.uni-bremen.de/index.php?again=yes")
//            .data("loginname", "deniz7", "password", "xSh4d0wZ3r0_+nbrmn")
//            .method(Connection.Method.POST)
//            .execute()
//        val doc = Jsoup.connect("https://elearning.uni-bremen.de/dispatch.php/calendar/schedule").get()
//        Log.d("HELLO", doc.toString())
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

    }
}