package com.example.cryptoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.cryptoapp.modual.watchlist.WatchlistFragment

class WebviewActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var webView: WebView
    private lateinit var ima_back: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        InIt()

    }

    private fun InIt() {
        webView = findViewById(R.id.webView)
        ima_back = findViewById(R.id.ima_back)
        ima_back.setOnClickListener(this)
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()

        // this will load the url of the website
        webView.loadUrl(intent.getStringExtra("newsUrl").toString())
//        webView.loadUrl("www.google.com")

        webView.settings.javaScriptEnabled = true
        webView.settings.setDomStorageEnabled(true)
        webView.settings.allowContentAccess = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)


    }


    // if you press Back button this code will work
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (webView.canGoBack())
            webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id) {
            R.id.ima_back -> {
                onBackPressed()
            }

        }
    }
}