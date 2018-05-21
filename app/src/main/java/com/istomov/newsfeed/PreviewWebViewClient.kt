package com.istomov.newsfeed

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class PreviewWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view?.loadUrl(request?.url.toString())
        return true
    }
}
