package com.keyontech.meh3.viewpager1

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by kot on 2/17/18.
 */


class DepthViewPagerPageTransform: ViewPager.PageTransformer {
    companion object {
        private val MIN_SCALE = 0.75f
    }

    override fun transformPage(page: View?, position: Float) {
        val pageWidth = page?.width ?: return
        var vW = 0

        if (position < -1) {
            vW = 0
            page?.alpha = vW.toFloat()
        } else if (position <= 0) {
            vW = 1
            page?.alpha = vW.toFloat()

            vW = 0
            page?.translationX = vW.toFloat()

            vW = 1
            page?.scaleX = vW.toFloat()
            page?.scaleY = vW.toFloat()
        } else if (position <= 1) {
            page?.alpha = (1 - position)
            page?.translationX = -position * pageWidth

            val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position))
            page?.scaleX = scaleFactor
            page?.scaleY = scaleFactor
        } else {
            vW = 0
            page?.alpha = vW.toFloat()
        }
    }
}