package com.keyontech.meh3.adaptersViewPager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.jonesq.meh3.Models.ModelMeh
import com.example.jonesq.meh3.Models.ModelMehDeal

//import com.keyontech.meh3.models.MockModel
import com.keyontech.meh3.viewpager1.FragmentViewPager1
//import com.keyontech.meh3.viewpager2.FragmentViewPager2

/**
 * Created by kot on 1/14/18.
 */
class AdapterViewPagerActivityMain (fragmentManager: FragmentManager, private val modelMehDealPhotos: ArrayList<String>) :
        FragmentStatePagerAdapter(fragmentManager) {

    // 2
    override fun getItem(position: Int): Fragment {
        return FragmentViewPager1.newInstance(modelMehDealPhotos[position])
    }

    // 3
    override fun getCount(): Int {
        return modelMehDealPhotos.size
    }
}