package com.keyontech.meh3.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import com.keyontech.meh3.R
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by kot on 1/30/18.
 */

class ActivityAbout: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        /*** animate on start */
        cardView_ActivityAbout_1.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
        cardView_ActivityAbout_2.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
        cardView_ActivityAbout_3.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
        cardView_ActivityAbout_4.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
        cardView_ActivityAbout_5.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
        cardView_ActivityAbout_6.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
        cardView_ActivityAbout_7.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
        cardView_ActivityAbout_8.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
        cardView_ActivityAbout_9.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
    }
}