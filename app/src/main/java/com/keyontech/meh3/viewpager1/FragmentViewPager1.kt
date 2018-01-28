package com.keyontech.meh3.viewpager1

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.jonesq.meh3.utils.KEY_PHOTO_URI
import com.keyontech.meh3.R
import com.squareup.picasso.Picasso

class FragmentViewPager1 : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {
        // Creates the view controlled by the fragment
        val view = inflater.inflate(R.layout.viewpager_header_content_activitymain, container, false)
        val imageviewDealPhoto = view.findViewById<ImageView>(R.id.imageView_Deal_Photos)



        // Retrieve and display the movie data from the Bundle
        val args = arguments

        // Download the image and display it using Picasso
        Picasso.with(activity)
//            .downloader(new OkHttpDownloader( context , Integer.MAX_VALUE)
            .load( args.getString(KEY_PHOTO_URI) )
            .into(imageviewDealPhoto)
        return view
    }



// had to use this because it does not work in onCreateView fragment is not ready yet everything is null
//    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }




//    companion object {
//
//        // Method for creating new instances of the fragment
//        fun newInstance(mockModel: MockModel): FragmentViewPager1 {
//            // Store the mockModel data in a Bundle object
//            val args = Bundle()
//            args.putString(JSONreaderMockData.KEY_TITLE, mockModel.title)
//            args.putInt(JSONreaderMockData.KEY_RATING, mockModel.rating)
//            args.putString(JSONreaderMockData.KEY_POSTER_URI, mockModel.posterUri)
//            args.putString(JSONreaderMockData.KEY_OVERVIEW, mockModel.overview)
//
//            // Create a new mockFragment and set the Bundle as the arguments
//            // to be retrieved and displayed when the view is created
//            val fragment = FragmentViewPager1()
//            fragment.arguments = args
//            return fragment
//        }
//    }



    companion object {
        // Method for creating new instances of the fragment
        fun newInstance(photoURL: String): FragmentViewPager1 {
            println("photoURL: String = " + photoURL)

            // Store the mockModel data in a Bundle object
            val args = Bundle()
            args.putString(KEY_PHOTO_URI, photoURL)

            // Create a new mockFragment and set the Bundle as the arguments
            // to be retrieved and displayed when the view is created
            val fragment = FragmentViewPager1()
            fragment.arguments = args
            return fragment
        }
    }




}
