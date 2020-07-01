package com.thecrimsonpizza.tvtrackerkotlin.app.ui.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.URL_WEBVIEW
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        web_view.webViewClient = WebViewClient()
        web_view.settings.userAgentString = "Android"
        web_view.settings.javaScriptEnabled = true
        web_view.settings.setAppCacheEnabled(true)
        web_view.loadUrl(Intent().getStringExtra(URL_WEBVIEW))
    }
}