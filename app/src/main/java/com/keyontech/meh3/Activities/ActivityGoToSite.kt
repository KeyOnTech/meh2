package com.keyontech.meh3.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.keyontech.meh3.utils.ACT_EXTRA_GO_TO_SITE_URL
import com.keyontech.meh3.R
import com.keyontech.meh3.R.id.webView_activity_meh_video
import kotlinx.android.synthetic.main.activity_go_to_site_url.*

/**
 * Created by kot on 1/29/18.
 */

class ActivityGoToSite : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_to_site_url)
        val urlLink = intent.getStringExtra(ACT_EXTRA_GO_TO_SITE_URL)
/***        to make the webview allow java from pages add the following - start */
        webView_activity_go_to_site_url.settings.javaScriptEnabled = true
        webView_activity_go_to_site_url.settings.loadWithOverviewMode = true
        webView_activity_go_to_site_url.settings.useWideViewPort = true
/***        to make the webview allow java from pages add the following - end */
        webView_activity_go_to_site_url.loadUrl(urlLink)
    }
}