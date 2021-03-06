package com.istomov.newsfeed

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
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

    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.preview_progress_bar)
        webView = findViewById(R.id.webview_preview)

        toolbar.let {
            setSupportActionBar(it)
            it.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            it.setNavigationOnClickListener({ onBackPressed() })
        }

        webView.isScrollbarFadingEnabled = false
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) = onFirstPageLoadingFinished()
        }

        url = intent.getStringExtra(EXTRA_URL)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate menu resource file.
        menuInflater.inflate(R.menu.menu_preview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item == null -> super.onOptionsItemSelected(item)
            item.itemId == R.id.menu_item_share -> {
                startActivity(prepareShareAction())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun prepareShareAction(): Intent {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Have a look at this link! $url")
        sendIntent.type = "text/plain"
        return sendIntent
    }
}
