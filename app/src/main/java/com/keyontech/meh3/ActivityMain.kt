package com.keyontech.meh3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.jonesq.meh3.Models.ModelMeh
import com.google.gson.GsonBuilder
import com.keyontech.meh3.adaptersViewPager.AdapterViewPagerActivityMain

import kotlinx.android.synthetic.main.content_activity_main.*
import kotlinx.android.synthetic.main.activity_main_v2_nav_drawer.*
import kotlinx.android.synthetic.main.activity_main_v2_nav_drawer_include_content.*

import okhttp3.*
import org.json.JSONException
import java.io.IOException
import com.squareup.picasso.Picasso
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import com.example.jonesq.meh3.Models.*
import com.keyontech.meh3.Activities.ActivityAbout
import com.keyontech.meh3.Activities.ActivityMehPoll
import com.keyontech.meh3.Activities.ActivityMehVideo
import android.os.Handler
import android.os.HandlerThread
import android.preference.PreferenceManager
import com.example.jonesq.meh3.utils.*
import com.keyontech.meh3.Activities.ActivityGoToSite
import com.keyontech.meh3.services.BroadcastReceiver_Notifications_Service_Startup
import com.keyontech.meh3.services.IntentService_Notifications_Poll_Service
import kotlinx.android.synthetic.main.activity_meh_video.*
import kotlinx.android.synthetic.main.nav_drawer_layout.*


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
     * viewPager tab icons
     * https://stackoverflow.com/questions/38459309/how-do-you-create-an-android-view-pager-with-a-dots-indicator
     *
     * picasso notifications
     * https://futurestud.io/tutorials/picasso-callbacks-remoteviews-and-notifications
     *
     * store last call to meh.com in app preferences then when the app opens use that as the mockdata
     *
     * when the notification call is made store that in app preferencse as responseJSON for the mockdata
     *
     * this way without internet it still works
     *
     * add a refresh button to the nav drawer
     *
     *
     */








    /*** api request url */
    var jsonURL = ""
    /*** api response string */
    var jsonResponse = ""

    var mehVideoLink = ""

//    var navBackgroundColor = 0

    /*** this is used for the notification large image */
    var mehNotificationLargePhoto = ""

    /*** Broadcast Receiver  */
//    lateinit var receiver : BroadcastReceiver
    // make broadcastreceiver work on api 26
//    lateinit var broadcastReceiverContext : Context

    // define View Pager
    private lateinit var adapterActivityMain: AdapterViewPagerActivityMain








    companion object {
        var mehDealUrl: String = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_v2_nav_drawer)
        setSupportActionBar(toolbarNavDrawer)


// setup Nav Drawer
        var toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbarNavDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


// turn on auto check every night --- start
        if (!IntentService_Notifications_Poll_Service.isServiceAlarmOn(this)) {
            println("222  setupAlarm_To_Auto_Check_EveryNight")
            setupAlarm_To_Auto_Check_EveryNight()
        } // if


// get url from file
// fetch data
        var gson = GsonBuilder().create()
        var urlFile = loadJsonFromFile("url.json", this)
        var jsonOutput = gson.fromJson( urlFile , JSONUrL::class.java )
        jsonURL = jsonOutput.url

        setTitle("")
        fetchJSON()
//        mockInterface()


// fab buton Right
        fabbuttonNavDrawer.setOnClickListener { view ->
//            setTitle("")
//            fetchJSON()
//            mockInterface()

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            var jsonResponse = sharedPreferences.getString(KEY_MEH_RESPONSE_STRING, "")
            println("sharedPreferences  :  jsonResponse = " + jsonResponse)

            goToURL(mehDealUrl)
            Snackbar.make(view, "GoTo site", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }


    fun goToURL(pURL: String) {
        println("pUrl = " + pURL)
        var intent = Intent(baseContext, ActivityGoToSite::class.java)
        intent.putExtra(EXTRA_GO_TO_SITE_URL, pURL)
        startActivity(intent)
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
            R.id.nav_bar_poll -> {
                var intent = Intent(baseContext, ActivityMehPoll::class.java)
                intent.putExtra(KEY_MEH_RESPONSE_STRING, jsonResponse)
                startActivity(intent)
            }
            R.id.nav_bar_video-> {
                var intent = Intent(baseContext, ActivityMehVideo::class.java)
                intent.putExtra(KEY_MEH_VIDEO_LINK, mehVideoLink)
                startActivity(intent)
            }
            R.id.nav_bar_about-> {
                var intent = Intent(baseContext, ActivityAbout::class.java)
                startActivity(intent)
            }
            R.id.nav_bar_refresh-> {
                fetchJSON()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }











    fun fetchJSON() {
        var request = Request.Builder().url( jsonURL ).build()
        var client = OkHttpClient()

        /*** had to enqueue because you cannot execute in the main method needs a thread to do so */
//        client.newCall( request ).execute()
        client.newCall( request ).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
//                println("111aaa LOAD : live data")

                var responseBody = response?.body()?.string()
//                println( "111bbb  fetchJSON - onResponse - body - " + responseBody )
                jsonResponse = responseBody.toString()
                processReturn(jsonResponse)
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("fetchJSON - failed to execute request - error: " + e.toString() )

                try {
                    println("LOAD :  shared prefs data")
                    // get JSON response from prefs
                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    var jsonResponse = sharedPreferences.getString(KEY_MEH_RESPONSE_STRING, "")
                    println("sharedPreferences  :  jsonResponse " + jsonResponse)

                    if (jsonResponse.isEmpty() ) {
                        println("LOAD :  MOCK data")
                        // get JSON response from assets file
                        var mockData = loadJsonFromFile("sample1.json", applicationContext)
                        jsonResponse = mockData
                    }

                    processReturn(jsonResponse)
                } catch (e: JSONException) {
                    println("onFailure Catch")
                } // try
            }
        })
    } //



















    fun mockInterface() {
        println("Mock Data initiated ")

        try {
            println("LOAD :  MOCK data")
            var mockData = loadJsonFromFile("sample1.json", this)
            jsonResponse = mockData

            processMOCKReturn(jsonResponse)
        } catch (e: JSONException) {
            println("mockInterface Error: " + e)
        } // try

    } //



    fun processReturn(response: String){
        var gson = GsonBuilder().create()
        var modelMeh = gson.fromJson( response , ModelMeh::class.java )


        println( "222bbb  modelMeh.deal = " + modelMeh.deal )
        println( "222bbb  modelMeh.deal title = " + modelMeh.deal.title )

        println( "222ccc  modelMeh.deal.theme.accentColor = " + modelMeh.deal.theme.accentColor )
        println( "222ccc  modelMeh.deal.theme.backgroundColor = " + modelMeh.deal.theme.backgroundColor )


//        mehPoll = modelMeh.poll

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








        // setup interface
        runOnUiThread{
//          setTitle(priceLowtoHigh(modelMeh.deal.title))

            // set the color scheme  --- START
            try {
                if (!(modelMeh.deal.theme.accentColor === "" || modelMeh.deal.theme.accentColor == null) || !(modelMeh.deal.theme.backgroundColor === "" || modelMeh.deal.theme.backgroundColor == null)) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        // Lollipop and above colors
                        window.statusBarColor = Color.parseColor(modelMeh.deal.theme.accentColor)
                        window.navigationBarColor = Color.parseColor(modelMeh.deal.theme.accentColor)
                    }

                    try {
                        toolbarNavDrawer.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))

                        // set the custom color for the tabs
                        viewPager_NavDrawer.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))
                        //                    mViewPager_Tabs.setSelectedIndicatorColors(Color.parseColor(modelMeh.deal.theme.accentColor ) );
                    } catch (e: Exception) {
                        printToErrorLog_10("ActivityMain", "runOnUiThread")
                    }

                    nav_drawer_header_linear_layout.setBackgroundColor( Color.parseColor(modelMeh.deal.theme.accentColor))
//                    nav_drawer_header_linear_layout.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))
                }
            } catch (e: JSONException) {
                printToErrorLog_10("ActivityMain", "runOnUiThread try")
            }

            // set body text
            textView_content_activity_main_card_view_1.text = modelMeh.deal.title
            textView_content_activity_main_card_view_4.text = priceLowtoHigh(modelMeh.deal)
            textView_content_activity_main_card_view_2.text = modelMeh.deal.features
            textView_content_activity_main_card_view_3.text = modelMeh.deal.specifications

            // set video
            mehVideoLink = modelMeh.video.topic.url

            // set site URL
//            mehDealUrl = "https://meh.com/"
            mehDealUrl = modelMeh.deal.url

            // save string to preferences
            PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(KEY_MEH_RESPONSE_STRING, response)
                .apply()

            // set notification large image
            mehNotificationLargePhoto = modelMeh.deal.photos[0]

            // set photos viewPager
            adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, modelMeh.deal.photos )
            viewPager_NavDrawer.adapter = adapterActivityMain

            // setup viewPager indicator buttons
            tab_layout_viewpager_indicator_dots_NavDrawer.setupWithViewPager(viewPager_NavDrawer,true)

/***
            // display notification
            createNotification(
                this
                ,"Meh"
                ,modelMeh.deal.title
                ,priceLowtoHigh(modelMeh.deal)
                ,R.drawable.logo_32_x_32_2
                ,R.drawable.logo_32_x_32_2
                ,mehNotificationLargePhoto
                ,R.drawable.logo_32_x_32_2
            )
*/
        }
    }








    fun processMOCKReturn(response: String){
        var gson = GsonBuilder().create()
        var modelMeh = gson.fromJson( response , ModelMeh::class.java )


        println( "222bbb  modelMeh.deal = " + modelMeh.deal )
        println( "222bbb  modelMeh.deal title = " + modelMeh.deal.title )

        println( "222ccc  modelMeh.deal.theme.accentColor = " + modelMeh.deal.theme.accentColor )
        println( "222ccc  modelMeh.deal.theme.backgroundColor = " + modelMeh.deal.theme.backgroundColor )


//        mehPoll = modelMeh.poll

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








        // setup interface
//        runOnUiThread{
            //          setTitle(priceLowtoHigh(modelMeh.deal.title))

            // set the color scheme  --- START
            try {
                if (!(modelMeh.deal.theme.accentColor === "" || modelMeh.deal.theme.accentColor == null) || !(modelMeh.deal.theme.backgroundColor === "" || modelMeh.deal.theme.backgroundColor == null)) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        // Lollipop and above colors
                        window.statusBarColor = Color.parseColor(modelMeh.deal.theme.accentColor)
                        window.navigationBarColor = Color.parseColor(modelMeh.deal.theme.accentColor)
                    }

                    try {
                        toolbarNavDrawer.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))

                        // set the custom color for the tabs
                        viewPager_NavDrawer.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))
                        //                    mViewPager_Tabs.setSelectedIndicatorColors(Color.parseColor(modelMeh.deal.theme.accentColor ) );
                    } catch (e: Exception) {
                        printToErrorLog_10("ActivityMain", "runOnUiThread")
                    }

//                    navBackgroundColor = Color.parseColor(modelMeh.deal.theme.accentColor)
//                    nav_drawer_header_linear_layout.setBackgroundColor( Color.parseColor(modelMeh.deal.theme.accentColor))
////                    nav_drawer_header_linear_layout.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))
                }
            } catch (e: JSONException) {
                printToErrorLog_10("ActivityMain", "runOnUiThread try")
            }


//            navBackgroundColor = Color.parseColor(modelMeh.deal.theme.accentColor)
//            nav_drawer_header_linear_layout.setBackgroundColor(navBackgroundColor)


            // set body text
            textView_content_activity_main_card_view_1.text = modelMeh.deal.title
            textView_content_activity_main_card_view_4.text = priceLowtoHigh(modelMeh.deal)
            textView_content_activity_main_card_view_2.text = modelMeh.deal.features
            textView_content_activity_main_card_view_3.text = modelMeh.deal.specifications

            // set video
            mehVideoLink = modelMeh.video.topic.url

            // set site URL
//            mehDealUrl = "https://meh.com/"
            mehDealUrl = modelMeh.deal.url

            // save string to preferences
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(KEY_MEH_RESPONSE_STRING, response)
                    .apply()

            // set notification large image
            mehNotificationLargePhoto = modelMeh.deal.photos[0]

            // set photos viewPager
            adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, modelMeh.deal.photos )
            viewPager_NavDrawer.adapter = adapterActivityMain

            // setup viewPager indicator buttons
            tab_layout_viewpager_indicator_dots_NavDrawer.setupWithViewPager(viewPager_NavDrawer,true)

            /***
            // display notification
            createNotification(
            this
            ,"Meh"
            ,modelMeh.deal.title
            ,priceLowtoHigh(modelMeh.deal)
            ,R.drawable.logo_32_x_32_2
            ,R.drawable.logo_32_x_32_2
            ,mehNotificationLargePhoto
            ,R.drawable.logo_32_x_32_2
            )
             */
//        }
    }



/***
    private fun createNotification( pContext: Context, tickerText: String = "", notificationTitle: String = "", notificationText: String, showactionRightButtonIcon: Int, showactionLeftButtonIcon: Int, largebitmapImageURL : String, smallIcon : Int) {
        var handlerThread = HandlerThread("aaa")
        handlerThread.start()

        var handler = Handler(handlerThread.getLooper())
        handler.post(Runnable {
            var notificationLargeBitmap: Bitmap? = null
            try {
                notificationLargeBitmap  = Picasso
                        .with(pContext)
                        .load(largebitmapImageURL)
                        .resize(512,512)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .get()

                showNotification(
                        pContext
                        ,tickerText
                        ,notificationTitle
                        ,notificationText
                        ,showactionRightButtonIcon
                        ,showactionLeftButtonIcon
                        ,notificationLargeBitmap
                        ,smallIcon
                )
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (notificationLargeBitmap != null) {
                    //do whatever you wanna do with the picture.
                    //for me it was using my own cache
//                    imageCaching.cacheImage(imageId, bitmap)
                }
            }
        })
    }
*/














/***
    fun broadcastReceiver2() {
        // make broadcastreceiver work on api 26
        broadcastReceiverContext = this
        sendBroadcast(Intent( broadcastReceiverContext , BroadcastReceiver_Notifications_Service_Startup::class.java) )
//        sendBroadcast(Intent("ManifestMyReceiver") )
    }
*/





//    // https://www.youtube.com/watch?v=nDzwiacP4aQ
//    fun broadcastReceiver() {
//        var filter = IntentFilter()
//        filter.addAction(Intent.ACTION_BOOT_COMPLETED)
////        filter.addAction(Intent.Actph)
////        filter.addAction(Intent.ACTION_POWER_CONNECTED)
////        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)
//
//        receiver = object : BroadcastReceiver() {
//            override fun onReceive(p0: Context?, p1: Intent?) {
//                Toast.makeText(p0, "fun broadcastReceiver " + p1?.action, Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        registerReceiver(receiver,filter)
//    }
//
//    override fun onDestroy() {
//        unregisterReceiver(receiver)
//        super.onDestroy()
//    }









    fun setupAlarm_To_Auto_Check_EveryNight() {
        println("333  set alarm to check every night stared ")
        var shouldStartAlarm = true
        //            printToToast( this , TAG , "Should I start the polling notification True or False??? " + shouldStartAlarm ); ;
        IntentService_Notifications_Poll_Service.setServiceAlarm(this , shouldStartAlarm)  // .set.setServiceAlarm(this, shouldStartAlarm)
    } // setPollingNotifications

}