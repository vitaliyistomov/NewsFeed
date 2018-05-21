package com.istomov.newsfeed

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

const val EXTRA_URL = "url_to_preview"

class PreviewActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.preview_progress_bar)
        webView = findViewById(R.id.webview_preview)

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener({ onBackPressed() })

        webView.isScrollbarFadingEnabled = false
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) = onFirstPageLoadingFinished()
        }

        val url = intent.getStringExtra(EXTRA_URL)
        webView.loadUrl(url)
    }

    private fun onFirstPageLoadingFinished() {
        webView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
