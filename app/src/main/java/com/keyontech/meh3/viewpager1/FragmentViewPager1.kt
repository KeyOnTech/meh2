package com.keyontech.meh3.viewpager1

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.keyontech.meh3.R
import com.keyontech.meh3.utils.FRAG_ARG_PHOTO_URI
import com.squareup.picasso.Picasso

class FragmentViewPager1 : Fragment() {
    companion object {
        // Method for creating new instances of the fragment
        fun newInstance(photoURL: String): FragmentViewPager1 {
            /*** Store the mockModel data in a Bundle object */
            val args = Bundle()
            args.putString(FRAG_ARG_PHOTO_URI, photoURL)

            /***
             * Create a new mockFragment and set the Bundle as the arguments
             * to be retrieved and displayed when the view is created
            */
            val fragment = FragmentViewPager1()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? {

            // Creates the view controlled by the fragment
            val view = inflater.inflate(R.layout.viewpager_header_content_activitymain, container, false)
            val imageviewDealPhoto = view.findViewById<ImageView>(R.id.imageView_Deal_Photos)

            // Retrieve and display the movie data from the Bundle
            val args = arguments

//            println("888aaaa --------- ")

            // Download the image and display it using Picasso
            val picassoImage = Picasso.with(activity)
            /*** color codes: Red: Network / Blue: Disk / Yellow: Memory - dev preferred not on live apk */
            picassoImage.setIndicatorsEnabled(true)

            if (args.getString(FRAG_ARG_PHOTO_URI) != null && args.getString(FRAG_ARG_PHOTO_URI).isNotEmpty()) {
                picassoImage
                        //            .with(activity) /*** set during declaration above  */
                        ////            .downloader(new OkHttpDownloader( context , Integer.MAX_VALUE)
                        ////            .centerCrop()
                        ////           .resize(512,512)
                        .load(args.getString(FRAG_ARG_PHOTO_URI))
                        .placeholder(R.drawable.ic_failed_to_load_image)
                        .error(R.drawable.ic_failed_to_load_image)
                        .into(imageviewDealPhoto)
            } else {
                picassoImage
                        .load(R.drawable.ic_failed_to_load_image)
                        .placeholder(R.drawable.ic_failed_to_load_image)
                        .error(R.drawable.ic_failed_to_load_image)
                        .into(imageviewDealPhoto)
            }

        return view
    }

// had to use this because it does not work in onCreateView fragment is not ready yet everything is null
//    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

}