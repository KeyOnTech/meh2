//package com.keyontech.meh3.adapters
//
//import android.content.Context
//import android.support.v4.view.PagerAdapter
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.LinearLayout
//import com.keyontech.meh3.R
//import com.squareup.picasso.Picasso
//import java.util.ArrayList
//
//class AdapterViewPagerBackgroundImageKT (internal var context: Context, internal var arrayList: ArrayList<String>?) : PagerAdapter() {
//    internal var layoutInflater: LayoutInflater
//
//    init {
//        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    }
//
//    override fun getCount(): Int {
//        return if (arrayList != null) {
//            arrayList!!.size
//        } else 0
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view === `object` as LinearLayout
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val itemView = layoutInflater.inflate(R.layout.image_viewpager_layout, container, false)
//
//        val imageView = itemView.findViewById(R.id.viewPagerItem_image1) as ImageView
//
//        Picasso.with(context).load(arrayList!![position])
//                .placeholder(R.drawable.image_uploading)
//                .error(R.drawable.image_not_found).into(imageView)
//
//        container.addView(itemView)
//
//        return itemView
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as LinearLayout)
//    }
//
//}