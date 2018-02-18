package com.keyontech.meh3.viewpager1

import android.support.v4.view.ViewPager
import android.view.View
import com.keyontech.meh3.R

/**
 * Created by kot on 2/17/18.
 *
 * //        var vX = 0.5 // works
//        var vX = 0.75 // works
//        var vX = 1 // works
//        var vX = 2 // works
//        var vX = 50 // works


//        var vX = 0.5 // works
//        var vX = 0.75 // works
//        var vX = 1 // works
//        var vX = 2 // works

//        var vY = 0.5 // works
//        var vY = 0.75 // works
//        var vY = 1 // works
//        var vY = 2 // works
//        var vY = 5 // works
//        var vY = 7 // works
//        var vY = 10 // works
 */


class TopRightToBottomLeftViewPagerPageTransform: ViewPager.PageTransformer {
    var fragmentImage: View? = null

    override fun transformPage(page: View?, position: Float) {
        val pageWidth = page?.width ?: return

        /*** swipe comes from top right down to bottom left */
        var vX = 50 // works
        var vY = 0.75 // works

        fragmentImage = page.findViewById(R.id.imageView_Deal_Photos)
        /*** use this to make a slide over effect when swiping */
        fragmentImage?.translationX = -position * (pageWidth / vX.toFloat()) // works
        /*** use to make the image come down / up from the top / bottom */
        fragmentImage?.translationY = -position * (pageWidth / vY.toFloat()) // works
    }
}