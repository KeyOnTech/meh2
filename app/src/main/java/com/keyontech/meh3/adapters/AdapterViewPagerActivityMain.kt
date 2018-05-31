package com.keyontech.meh3.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.keyontech.meh3.viewpager1.FragmentViewPager1

/**
 * Created by kot on 1/14/18.
 */
//class AdapterViewPagerActivityMain (fragmentManager: FragmentManager, private val modelMehDealPhotos: ArrayList<String>, private val modelMehDealPhotosBackground: String) :
class AdapterViewPagerActivityMain (fragmentManager: FragmentManager, private val modelMehDealPhotos: ArrayList<String>) :
        FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
//        return FragmentViewPager1.newInstance(modelMehDealPhotos[position], modelMehDealPhotosBackground)
        return FragmentViewPager1.newInstance(modelMehDealPhotos[position])
    }

    override fun getCount(): Int {
        return modelMehDealPhotos.size
    }
}