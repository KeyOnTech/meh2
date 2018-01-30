package com.keyontech.meh3

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.example.jonesq.meh3.Models.ModelMeh
//import com.example.jonesq.meh3.utils.JSONurl
import com.google.gson.GsonBuilder
import com.keyontech.meh3.adaptersViewPager.AdapterViewPagerActivityMain
//import com.keyontech.meh3.Models.ModelMeh
//import com.keyontech.meh3.utils.JSONurl
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_nav_drawer.*

//import kotlinx.android.synthetic.main.app_bar_activity_nav_drawer.*
//import kotlinx.android.synthetic.main.activity_main_v2_nav_drawer.*


import kotlinx.android.synthetic.main.content_activity_main.*
import kotlinx.android.synthetic.main.activity_main_v2_nav_drawer.*
import kotlinx.android.synthetic.main.activity_main_v2_nav_drawer_include_content.*
import kotlinx.android.synthetic.main.content_activity_main.*
import kotlinx.android.synthetic.main.nav_drawer_layout.*

import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import com.squareup.picasso.Picasso
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.jonesq.meh3.Models.ModelMehDeal









import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import com.example.jonesq.meh3.Models.*
import com.example.jonesq.meh3.utils.KEY_MEH_VIDEO_LINK
import com.keyontech.meh3.Activities.ActivityMehVideo

//import android.support.v7.app.AppCompatActivity
//import android.view.Menu
//import android.view.MenuItem




//class ActivityMain : AppCompatActivity() {
class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    /***
     * Issues to resolve
     *
     * notification at 11pm
     * ask for permission to use the internet
     * check if not allowed app wont work
     * shows mock data
     *
     * picasso cache the photos
     *
     * Move constants to Strings Resource file
     *
     *
     */









    var jsonURL = ""

    var abcgogo = ""
    var mehVideoLink = ""

//    val modelMeh = object
//    val pBitmap_Map = BitmapFactory.decodeResource(resources, R.drawable.logo_512_x_512_2)


    // define View Pager
    private lateinit var adapterActivityMain: AdapterViewPagerActivityMain



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_v2_nav_drawer)
//        setContentView(R.layout.activity_main)
//        setSupportActionBar(NavDrawer)
        setSupportActionBar(toolbarNavDrawer)


// setup Nav Drawer
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbarNavDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)





// fetch data
        val gson = GsonBuilder().create()
        val urlFile = loadJsonFromFile("url.json", this)
        val jsonOutput = gson.fromJson( urlFile , JSONUrL::class.java )
        jsonURL = jsonOutput .url

        setTitle("")
//        fetchJSON()
        mockInterface()





// fab buton Right
        fabbuttonNavDrawer.setOnClickListener { view ->
//            val intentA = Intent(view.context, ActivityNavDrawer::class.java)
////            intent.putExtra( KEY_COURSELESSONLINK , courseLesson?.link )
//            startActivity(intentA)


            setTitle("")
            fetchJSON()
//            mockInterface()



            // notification large icon
//            val pBitmap_Map = BitmapFactory.decodeResource(resources, R.drawable.logo_512_x_512_2)
//            val pBitmap_MapScaled = Bitmap.createScaledBitmap(pBitmap_Map, 96, 96, true)
//            val pShow_Large_Icon_Bitmap = Picasso
//                    .with(this)
//                    //                            .load( R.drawable.logo_32_x_32_2 )
////                    .load(pMeh_API_v1.getDeal_Details().getmPhotos().get(0))
////                    .load(R.drawable.logo_32_x_32_2)
//                    //                            .resize(32, 32)
//
////                    .placeholder(R.drawable.logo_32_x_32_2)
//                    .error(R.drawable.logo_32_x_32_2)
//                    .get()






            // create notification - start
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(this@ActivityMain,0,intent,0)
            val notification = Notification.Builder(this@ActivityMain)
                    .setTicker("")
                    .setContentTitle("")
                    .setContentText("")
                    .setSmallIcon(R.drawable.notification_bg)
//                    .setLargeIcon(pShow_Large_Icon_Bitmap)

                    .setContentIntent(pendingIntent).notification

            notification.flags = Notification.FLAG_AUTO_CANCEL
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)


            // crate notification - end

            Snackbar.make(view, "Live data", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }




    // nav drawer

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_bar_specifications -> {
                // Handle the camera action
            }
            R.id.nav_bar_poll -> {

            }
            R.id.nav_bar_video-> {
                val intent = Intent( baseContext , ActivityMehVideo::class.java)
                intent.putExtra( KEY_MEH_VIDEO_LINK , mehVideoLink )
                startActivity( intent )
            }
            R.id.nav_bar_about-> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }











    fun fetchJSON() {
        println("ACctiviyMain - onCreate - attempting to fetch JSON")
        println("url " + jsonURL)

        val request = Request.Builder().url( jsonURL ).build()
        val client = OkHttpClient()

        // had to enqueue because you cannot execute in the main method needs a thread to do so
//        client.newCall( request ).execute()
        client.newCall( request ).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string()
                println( "111  fetchJSON - onResponse - body - " + responseBody )

                val gson = GsonBuilder().create()

                val modelMeh = gson.fromJson( responseBody , ModelMeh::class.java )
                println( "222bbb  modelMeh.deal = " + modelMeh.deal )
                println( "222bbb  modelMeh.deal title = " + modelMeh.deal.title )

                println( "333aaa  modelMeh.poll = " + modelMeh.poll)
                println( "333bbb  modelMeh.poll title = " + modelMeh.poll.title )
                println( "333ccc  modelMeh.poll id = " + modelMeh.poll.id )
                println( "333ddd  modelMeh.poll startDate = " + modelMeh.poll.startDate )
                println( "333eee  modelMeh.poll answers[0].text = " + modelMeh.poll.answers[0].text )
                println( "333fff  modelMeh.poll answers[0].voteCount = " + modelMeh.poll.answers[0].voteCount )
                println( "333ggg  modelMeh.poll topic.url = " + modelMeh.poll.topic.url)



                println( " 77777777     modelMeh.video  " + modelMeh.video )
//                if (modelMeh.video == null )
                println( "444aaa  modelMeh.vieo = " + modelMeh.video)
                println( "444bbb  modelMeh.video title = " + modelMeh.video.title )
                println( "444ccc  modelMeh.video topic.url = " + modelMeh.video.topic.url)

                runOnUiThread{
                    println("LOAD : live data")

                    // setup interface
//                    setTitle(priceLowtoHigh(modelMeh.deal))

                    // set body text
                    textView_content_activity_main_card_view_1.text = modelMeh.deal.title
                    textView_content_activity_main_card_view_4.text = priceLowtoHigh(modelMeh.deal)
                    textView_content_activity_main_card_view_2.text = modelMeh.deal.features
                    textView_content_activity_main_card_view_3.text = modelMeh.deal.specifications

                    mehVideoLink = modelMeh.video.topic.url

                    adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, modelMeh.deal.photos )
                    viewPager_NavDrawer.adapter = adapterActivityMain
//                    viewPager_ActivityMain.adapter = adapterActivityMain


                    abcgogo = modelMeh.deal.photos[0]
                }


//                val myResponse = response.body().string()

//                this@ScrollingActivity.runOnUiThread(java.lang.Runnable { abcGo = responseBody?.toString() })
//                this@ScrollingActivity.runOnUiThread(java.lang.Runnable { abcGo = responseBody?; })

            }


            override fun onFailure(call: Call?, e: IOException?) {
                println("fetchJSON - failed to execute request - error: " + e.toString() )
            }
        })
    } //



















    fun mockInterface() {
        println("Mock Data initiated ")

        val gson = GsonBuilder().create()

        try {
            println("LOAD :  MOCK data")
            val mockData = loadJsonFromFile("sample1.json", this)
            val modelMeh = gson.fromJson( mockData , ModelMeh::class.java )

            // setup interface
//            setTitle(priceLowtoHigh(modelMeh.deal))

            // set body text
            textView_content_activity_main_card_view_1.text = modelMeh.deal.title
            textView_content_activity_main_card_view_4.text = priceLowtoHigh(modelMeh.deal)
            textView_content_activity_main_card_view_2.text = modelMeh.deal.features
            textView_content_activity_main_card_view_3.text = modelMeh.deal.specifications

            mehVideoLink = modelMeh.video.topic.url


            adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, modelMeh.deal.photos )
//            viewPager_ActivityMain.adapter = adapterActivityMain
            viewPager_NavDrawer.adapter = adapterActivityMain

        } catch (e: JSONException) {
            println("mockInterface Error: " + e)
        } // try

    } //


    private fun loadJsonFromFile(filename: String, context: Context): String {
        var json = ""

        try {
            val input = context.assets.open(filename)
            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()
            json = buffer.toString(Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return json
    } //




    fun priceLowtoHigh(modelMehDeal: ModelMehDeal): String {
        var vMin = modelMehDeal.items[0].price.toInt()
        var vMax = modelMehDeal.items[0].price.toInt()

        for (i in modelMehDeal.items) {
            if (vMin > i.price) vMin = i.price.toInt()
            if (vMax < i.price) vMax = i.price.toInt()
            print(" \r\n 44401: price " + i.price.toInt() )
        }

        if (vMin != vMax) {
            return "$ $vMin - $ $vMax"
        }else{
            return "$ $vMax"
        }
    }



} // activity
