package com.keyontech.meh3.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.jonesq.meh3.utils.*
import com.keyontech.meh3.R
import kotlinx.android.synthetic.main.activity_meh_video.*

/**
 * Created by kot on 1/29/18.
 */

class ActivityMehVideo: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_meh_video)
/***        webView_activity_meh_video.setBackgroundColor(Color.YELLOW) */
        val courseLink = intent.getStringExtra(KEY_MEH_VIDEO_LINK)
/***        to make the webview allow java from pages add the following - start */
        webView_activity_meh_video.settings.javaScriptEnabled = true
        webView_activity_meh_video.settings.loadWithOverviewMode = true
        webView_activity_meh_video.settings.useWideViewPort = true
/***        to make the webview allow java from pages add the following - end */
        webView_activity_meh_video.loadUrl(courseLink)
    }
}