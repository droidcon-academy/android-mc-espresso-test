package com.droidcon.droidflix.ui

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun FlixWebScreen() {
    val mUrl = "https://www.omdbapi.com/"
    AndroidView(factory = {
        WebView(it).apply {
            this.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.webViewClient = CustomWebViewClient()
            this.settings.javaScriptEnabled = true
        }
    }, update = {
        it.loadUrl(mUrl)
    })
}

class CustomWebViewClient: WebViewClient(){
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return url != null && url.startsWith("https://www.omdbapi.com/")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }
}