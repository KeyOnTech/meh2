package com.keyontech.meh3

import android.app.PendingIntent.getActivity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.jonesq.meh3.Models.ModelMeh
import com.google.gson.GsonBuilder
import com.keyontech.meh3.adapters.AdapterViewPagerActivityMain

import kotlinx.android.synthetic.main.content_activity_main.*
import kotlinx.android.synthetic.main.activity_main_v2_nav_drawer.*
import kotlinx.android.synthetic.main.activity_main_v2_nav_drawer_include_content.*

import okhttp3.*
import org.json.JSONException
import java.io.IOException
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.PersistableBundle

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import com.example.jonesq.meh3.Models.*
import com.keyontech.meh3.Activities.ActivityAbout
import com.keyontech.meh3.Activities.ActivityMehPoll
import com.keyontech.meh3.Activities.ActivityMehVideo
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.jonesq.meh3.utils.*
import com.keyontech.meh3.Activities.ActivityGoToSite
import com.keyontech.meh3.R.id.container
import com.keyontech.meh3.services.IntentService_Notifications_Poll_Service
import com.keyontech.meh3.services.MJobExecuter
import com.keyontech.meh3.services.MJobScheduler
import com.keyontech.meh3.viewpager1.*
import kotlinx.android.synthetic.main.nav_drawer_layout.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*


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
     *viewPager animations / transforms
     * http://myhexaville.com/2017/03/17/android-viewpager-transformations/
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
     * view pager infinite loop
     * https://www.raywenderlich.com/169774/viewpager-tutorial-android-getting-started-kotlin
     *
     *
     *
     *
     *
     *
     *
     *
     * update string xml file and upload to language screen so it can translate the text for you to spanish
     *
     *
     * pass data to jobScheduler
     * https://stackoverflow.com/questions/29343480/persistablebundle-pass-values-to-service-and-retrieve
     *
     * asynctask multiple paramters
     * https://freakycoder.com/android-notes-16-how-to-pass-multiple-primitive-parameters-to-asynctask-d51c2aee2afb
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



    /*** job scheduler */
    val JSCHEDULER_JOB_ID = 44448
    //    lateinit var mJobScheduler: JobScheduler
//    lateinit var mJobInfo: JobInfo
//    private var mJobScheduler: JobScheduler? = null
    lateinit var mJobScheduler: JobScheduler
    //    private var mJobScheduler: MJobScheduler? = null
    private var mJobInfo: JobInfo? = null
//    lateinit private var serviceComponent: ComponentName
//    private var persistableBundle: PersistableBundle? = null
//    lateinit var persistableBundle: PersistableBundle

    lateinit var mJobExecuter: MJobExecuter

    /***
     * job scheduler kotlin sample
     * https://github.com/googlesamples/android-JobScheduler
     * https://github.com/googlesamples/android-JobScheduler/blob/master/kotlinApp/app/src/main/java/com/example/android/jobscheduler/MainActivity.kt
     * */



    companion object {
        var mehDealUrl: String = ""
    } // companion object


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_v2_nav_drawer)
        setSupportActionBar(toolbarNavDrawer)


// setup Nav Drawer
        var toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbarNavDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
                // dont show nav bar on start
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
                        .edit()
                        .putBoolean(PREF_KEY_SHOW_NAV_DRAWER_ONSTART, false)
                        .apply()
            }
        }

        fabbuttonNavDrawer.visibility = View.VISIBLE
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        // close nav drawer - https://www.supinfo.com/articles/single/5610-android-navigation-drawer
        // did nav drawer just clsoe - https://github.com/kittinunf/RxMovieKotlin/blob/master/app/src/main/kotlin/com/taskworld/android/rxmovie/view/fragment/NavigationDrawerFragment.kt
//        // alternavitve method Defer code dependent on restoration of previous instance state.
//        drawer_layout.setDrawerListener(drawerToggle)
//        drawer_layout.post { drawerToggle!!.syncState() }

        nav_view.setNavigationItemSelectedListener(this)

// auto open nav drawer
        // show nav drawer first time
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        var showNavDrawerOnStart = sharedPreferences.getBoolean(PREF_KEY_SHOW_NAV_DRAWER_ONSTART, true)

        if (showNavDrawerOnStart) {
            drawer_layout.openDrawer(Gravity.LEFT)
        }

// turn on auto check every night --- start
        if (!IntentService_Notifications_Poll_Service.isServiceAlarmOn(this)) {
//            println("222  setupAlarm_To_Auto_Check_EveryNight")
            setupAlarm_To_Auto_Check_EveryNight()
        } // if

        /*** job scheduler */



        /*** Job scheduler Paramters and conditions  */
        val componentName = ComponentName(this, MJobScheduler::class.java)
//        serviceComponent = ComponentName(this, MJobScheduler::class.java)

//        val cn: ComponentName = ComponentName(context, WeatherWidgetProvider::class.java)
        val jobInfoBuilder = JobInfo.Builder(JSCHEDULER_JOB_ID, componentName)

        /*** Job scheduler conditions  */


        /*** Job scheduler extras */
//        println( "ActivityMain  ---  onCreate  ---  url " + mehDealUrl)
//
//        persistableBundle = PersistableBundle()
//        persistableBundle.putString(JS_PERSISTABLE_BUNDLE_DEAL_URL, mehDealUrl)
//        jobInfoBuilder.setExtras(persistableBundle)



        /*** Specify that this job should recur with the provided interval, not more than once per period. */
//        jobInfoBuilder.setPeriodic(JS_SCHEDULE_5_SECONDS) // time to run will not work 15 min is the minimum
        jobInfoBuilder.setPeriodic(JS_SCHEDULE_8_HOURS) // setPeriodic 8 hours time to run will not work 15 min is the minimum

// https://stackoverflow.com/questions/38344220/job-scheduler-not-running-on-android-n

        /*** Specify that this job should be delayed by the provided amount of time. */
        /*** USE FOR TESTING ONLY 5 */
//        jobInfoBuilder.setMinimumLatency(5000) // testing 5 sec.s  YOUR_TIME_INTERVAL

        jobInfoBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // require internet
        /*** Set whether or not to persist this job across device reboots.  */
        jobInfoBuilder.setPersisted(true) // keep running on system reboot


        /*** this is the job it self  */
        mJobInfo = jobInfoBuilder.build()
        /*** this is the jobs schedule  */
        mJobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//        (getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler).schedule(jobInfoBuilder.build())
//        mJobScheduler.mehDealUrl = mehDealUrl
        /*** start job */
        scheduleJob()















// get url from file
// fetch data
        var gson = GsonBuilder().serializeNulls().create() // include null opjects when null
        var urlFile = loadJsonFromFile("url.json", this)
        var jsonOutput = gson.fromJson( urlFile , JSONUrL::class.java )
        jsonURL = jsonOutput.url

        setTitle("")


        fetchJSON()
        // Kick off an {@link AsyncTask} to perform the network request
//        val fetchJSONAsyncTask2_1 = fetchJSONAsyncTask2()
//        fetchJSONAsyncTask2_1.execute()
////        val sFetchJSON_U = fetchJSON_U()
//        fetchMockInterface()


// fab buton Right
        fabbuttonNavDrawer.setOnClickListener { view ->
//            setTitle("")




//            fetchJSON()
//            fetchMockInterface()



            // test from other class fix this
//        IntentService_Notifications_Poll_Service.fetchJSON()
            // Kick off an {@link AsyncTask} to perform the network request
//        val task_fetchJSONAsyncTask3 = IntentService_Notifications_Poll_Service.Companion.fetchJSONAsyncTask3()
//        task_fetchJSONAsyncTask3.execute()



            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            var jsonResponse = sharedPreferences.getString(PREF_KEY_MEH_RESPONSE_STRING, "")
            println("sharedPreferences  :  jsonResponse = " + jsonResponse)

            goToURL(mehDealUrl)
            Snackbar.make(view, "GoTo site", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }



    fun scheduleJob() {
        println("MainActivity  -  scheduleJob")
        /*** start / schedule the job service  */
        mJobScheduler!!.schedule(mJobInfo!!)
//        Toast.makeText(this, "Job Scheduled", Toast.LENGTH_LONG).show()
    }

    fun stopScheduledJob() {
        println("MainActivity  -  stopScheduledJob")
        /*** cancel all job services by ID  */
        mJobScheduler!!.cancel(JSCHEDULER_JOB_ID)
//        Toast.makeText(this, "Job Stopped / Canceled", Toast.LENGTH_LONG).show()
    }






    fun goToURL(pURL: String) {
        println("pUrl = " + pURL)
        var intent = Intent(baseContext, ActivityGoToSite::class.java)
        intent.putExtra(ACT_EXTRA_GO_TO_SITE_URL, pURL)
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
                intent.putExtra(PREF_KEY_MEH_RESPONSE_STRING, jsonResponse)
                startActivity(intent)
            }
            R.id.nav_bar_video-> {
                if (mehVideoLink.isNotEmpty()) {
                    var intent = Intent(baseContext, ActivityMehVideo::class.java)
                    intent.putExtra(ACT_EXTRA_MEH_VIDEO_LINK, mehVideoLink)
                    startActivity(intent)
                }
            }
            R.id.nav_bar_about-> {
                var intent = Intent(baseContext, ActivityAbout::class.java)
                startActivity(intent)
            }
            R.id.nav_bar_rate_app-> {
                startActivity(rateApp(this))
            }
            R.id.nav_bar_share_app-> {
                startActivity(shareApp(this))
            }
            R.id.nav_bar_refresh-> {
                fetchJSON()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }







    fun setupAlarm_To_Auto_Check_EveryNight() {
//        println("333  set alarm to check every night stared ")
        var shouldStartAlarm = true
//        printToToast( this , TAG , "Should I start the polling notification True or False??? " + shouldStartAlarm ); ;
        IntentService_Notifications_Poll_Service.setServiceAlarm(this , shouldStartAlarm)  // .set.setServiceAlarm(this, shouldStartAlarm)
    } // setPollingNotifications







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
//                    println("LOAD :  shared prefs data")
//                    // get JSON response from prefs
//                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
//                    var jsonResponse = sharedPreferences.getString(PREF_KEY_MEH_RESPONSE_STRING, "")
//                    println("sharedPreferences  :  jsonResponse " + jsonResponse)

//                    if (jsonResponse.isEmpty() ) {
//                        println("LOAD :  MOCK data")
//                        // get JSON response from assets file
//                        var mockData = loadJsonFromFile("sample1.json", applicationContext)
//                        jsonResponse = mockData
//                    }

                    processReturn(jsonResponse)
                } catch (e: JSONException) {
                    println("onFailure Catch")
                } // try
            }
        })
    } //








    fun fetchMockInterface() {
        println("Mock Data initiated ")

        try {
            println("LOAD :  MOCK data")
//            var mockData = loadJsonFromFile("sample1.json", this)
            var mockData = loadJsonFromFile("sampleNULLsTopics.json", this)
            jsonResponse = mockData

            println("jsonResponse = " + jsonResponse)
            processMOCKReturn(jsonResponse)
        } catch (e: JSONException) {
            println("fetchMockInterface Error: " + e)
        } // try

    } //

    fun processReturn(response: String){
        // setup interface
        runOnUiThread{
            displayResults(response)
        }
    }













    fun displayResults(response: String) {
        println("88888bbbb ------ ")
        try {

            println("8888ccccccc ------")

            if (response != null && response.isNotEmpty()) {
                println("8888ddd  ------")

                var gson = GsonBuilder().serializeNulls().create()
                var modelMeh = gson.fromJson(response, ModelMeh::class.java)

                /***        test returns
                ////        println( "222bbb  modelMeh.deal = " + modelMeh.deal )
                ////        println( "222bbb  modelMeh.deal title = " + modelMeh.deal.title )
                ////
                ////        println( "222ccc  modelMeh.deal.theme.accentColor = " + modelMeh.deal.theme.accentColor )
                ////        println( "222ccc  modelMeh.deal.theme.backgroundColor = " + modelMeh.deal.theme.backgroundColor )
                ////
                ////
                //////        mehPoll = modelMeh.poll
                ////
                ////        println( "333aaa  modelMeh.poll = " + modelMeh.poll)
                ////        println( "333bbb  modelMeh.poll title = " + modelMeh.poll.title )
                ////        println( "333ccc  modelMeh.poll id = " + modelMeh.poll.id )
                ////        println( "333ddd  modelMeh.poll startDate = " + modelMeh.poll.startDate )
                ////        println( "333eee  modelMeh.poll answers[0].text = " + modelMeh.poll.answers[0].text )
                ////        println( "333fff  modelMeh.poll answers[0].voteCount = " + modelMeh.poll.answers[0].voteCount )
                //////        println( "333hhh  modelMeh.poll topic.url = " + modelMeh.poll.topic)
                //////        println( "333ggg  modelMeh.poll topic.url = " + modelMeh.poll.topic.url)
                ////
                ////
                ////        println( " 77777777     modelMeh.video  " + modelMeh.video )
                //////                if (modelMeh.video == null )
                ////        println( "444aaa  modelMeh.vieo = " + modelMeh.video)
                ////        println( "444bbb  modelMeh.video title = " + modelMeh.video.title )
                ////        println( "444ccc  modelMeh.video topic.url = " + modelMeh.video.topic.url)
                 */


                // setup video activity
                mehVideoLink = if (modelMeh.video != null) {
                    if (modelMeh.video.topic != null) {
                        if (modelMeh.video.topic.url != null && modelMeh.video.topic.url.isNotEmpty()) {
                            modelMeh.video.topic.url
                        } else {
                            ""
                        }
                    } else {
                        ""
                    }
                } else {
                    ""
                }
    //            var firstFeature = featureArray.getJSONObject(0)
    // parse json object with out errors - https://stackoverflow.com/questions/34938640/advoid-many-try-catch-blog-when-parse-json/34938745#34938745


                // set site URL
                if (modelMeh.deal != null) {
                    // set main activity body text
    //                setTitle(priceLowtoHigh(modelMeh.deal.title))
                    textView_content_activity_main_card_view_1.text = modelMeh.deal.title
                    textView_content_activity_main_card_view_2.text = modelMeh.deal.features
                    textView_content_activity_main_card_view_3.text = modelMeh.deal.specifications

                    if ((modelMeh.deal.items != null) && (modelMeh.deal.items[0].condition != "" && modelMeh.deal.items[0].condition != null)) {
                        textView_content_activity_main_card_view_4.text = modelMeh.deal.items[0].condition + " - " + priceLowtoHigh(modelMeh.deal)
                    } else {
                        textView_content_activity_main_card_view_4.text = priceLowtoHigh(modelMeh.deal)
                    }


                    // set the color scheme  --- START
                    if (modelMeh.deal.theme != null) {
                        if ((modelMeh.deal.theme.accentColor != "" || modelMeh.deal.theme.accentColor != null) || (modelMeh.deal.theme.backgroundColor != "" || modelMeh.deal.theme.backgroundColor != null)) {
    //                        if (Build.VERSION.SDK_INT >= 21) {
                            /*** Lollipop and above colors min sdk is 21 so if no longer needed */
                            window.statusBarColor = Color.parseColor(modelMeh.deal.theme.accentColor)
                            window.navigationBarColor = Color.parseColor(modelMeh.deal.theme.accentColor)
    //                        }

                            try {
                                toolbarNavDrawer.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))

                                // set the custom color for the tabs
                                viewPager_NavDrawer.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))
                                //                    mViewPager_Tabs.setSelectedIndicatorColors(Color.parseColor(modelMeh.deal.theme.accentColor ) );
                            } catch (e: Exception) {
                                printToErrorLog_10("ActivityMain", "runOnUiThread")
                            }

                            try {
                                nav_drawer_header_linear_layout.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.accentColor))
                                //                    nav_drawer_header_linear_layout.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))
                            } catch (e: Exception) {
                                //
                            }

                            try {
                                //                                fabbuttonNavDrawer.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))
                                //                                fabbuttonNavDrawer.back.backgroundTint =  (Color.parseColor(modelMeh.deal.theme.backgroundColor))

                                //                                fabbuttonNavDrawer.rippleColor = Color.parseColor(modelMeh.deal.theme.backgroundColor)
                                fabbuttonNavDrawer.rippleColor = Color.parseColor(modelMeh.deal.theme.accentColor)
                            } catch (e: Exception) {
                                //
                            }
                        }
                    }

                    // set fab action button link
                    if (modelMeh.deal.url != null && modelMeh.deal.url.isNotEmpty()) {
                        mehDealUrl = modelMeh.deal.url
                    } else {
                        mehDealUrl = ""
                        println("set Deal Url here")
                    }

                    // save string to preferences
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .edit()
                            .putString(PREF_KEY_MEH_RESPONSE_STRING, response)
                            .apply()

                    // set notification large image
                    if (modelMeh.deal.photos != null) {
                        if (modelMeh.deal.photos[0].isNotEmpty()) {
                            mehNotificationLargePhoto = modelMeh.deal.photos[0]
                        } else {
                            mehNotificationLargePhoto = ""
                            println("set default large photo image here")
                        }

                        // set photos viewPager
                        adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, modelMeh.deal.photos)
                        viewPager_NavDrawer.offscreenPageLimit = 3
                        viewPager_NavDrawer.adapter = adapterActivityMain

                        // set custom swipe animations
                        val randomNumberCreator = Random()
                        val randomNumber = randomNumberCreator.nextInt(3)

                        when (randomNumber) {
                            0 -> viewPager_NavDrawer.setPageTransformer(false, DepthViewPagerPageTransform())
                            1 -> viewPager_NavDrawer.setPageTransformer(false, TopRightToBottomLeftViewPagerPageTransform())
                            else -> viewPager_NavDrawer.setPageTransformer(false, ParallaxViewPagerPageTransform())
                        }

                        // setup viewPager indicator buttons
                        tab_layout_viewpager_indicator_dots_NavDrawer.setupWithViewPager(viewPager_NavDrawer, true)
                    } else {
                        println("set view pager to null repsonse image")
                    }

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
                } else {
                    val failedToLoadPhoto : ArrayList<String> = ArrayList()
                    failedToLoadPhoto .add("")

                    // set photos viewPager
                    adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, failedToLoadPhoto )
                    viewPager_NavDrawer.offscreenPageLimit = 1
                    viewPager_NavDrawer.adapter = adapterActivityMain

                    println("set all content boxes to to null repsonses")
                }
            }else{
                /*** no internet response */
                fabbuttonNavDrawer.visibility = View.GONE

                val failedToLoadPhoto : ArrayList<String> = ArrayList()
                failedToLoadPhoto .add("")

                // set photos viewPager
                adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, failedToLoadPhoto )
                viewPager_NavDrawer.offscreenPageLimit = 1
                viewPager_NavDrawer.adapter = adapterActivityMain

                /***  http://onetouchcode.com/2016/12/24/use-snackbar-android-apps/  */
                /***  https://androidteachers.com/android/android-material-design-snackbar-example/  */
                Snackbar.make(
                        findViewById(android.R.id.content)
                        , "Unable to fetch data please check internet connection"
                        , Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry") {
                            fetchJSON()
                            Snackbar.make(
                                    findViewById(android.R.id.content)
                                    , "Attempting to fetch data!"
                                    , Snackbar.LENGTH_SHORT).show()
                        }.show()


/***

                //                http://onetouchcode.com/2016/12/24/use-snackbar-android-apps/
                //                https://stackoverflow.com/questions/36474397/android-design-library-snackbar-getting-textview-returns-null
                //                https://antonioleiva.com/kotlin-awesome-tricks-for-android/

//                Snackbar.make(this, "Unable to fetch data please check internet connection", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show()

//                val snackbar = Snackbar.make(onCreatePanelView(), "Welcome To Main Activity", Snackbar.LENGTH_LONG)
//                snackbar.show()






                // works
//                Snackbar.make(findViewById(android.R.id.content), "Unable to fetch data please check internet connection", Snackbar.LENGTH_LONG).show();








//                val snackbar3 = Snackbar.make(
//                        findViewById(android.R.id.content)
//                        , "No internet connection!", Snackbar.LENGTH_LONG)
//                        .setAction("RETRY") { }
//
//                snackbar3.setActionTextColor(Color.GREEN)
//                val sbView = snackbar3.view
//                val textView = sbView.findViewById(android.support.design.R.id.snackbar_text) as TextView
//                textView.setTextColor(Color.RED)
//
//                snackbar3.show()








//                final Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Onetouchcode welcome's you", Snackbar.LENGTH_LONG);
//        snackbar.setAction("Close", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                snackbar.dismiss();
//            }
//        });
//        snackbar.setActionTextColor(Color.BLUE);
//        View snackbarView = snackbar.getView();
//        snackbarView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
//        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(Color.WHITE);
//        snackbar.show();














//                val snackbar = Snackbar.make(findViewById(android.R.id.content), "Onetouchcode welcome's you", Snackbar.LENGTH_LONG)
//                val snackbarView = snackbar.view
//                snackbarView.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark))
//
//
//                val textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
//                textView.setTextColor(Color.WHITE)
//
////                val textView = findViewById(android.support.design.R.id.snackbar_text)
////                textView.setTextColor(Color.WHITE)
//                snackbar.show()









////                val containerView = inflater.inflate(android.R.id.content, container, false)
////                val snackbar = Snackbar.make(containerView, "snackbar text", Snackbar.LENGTH_SHORT)
//                val snackbarView2 = snackbar.view
//                val textView = snackbarView2.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
////                text.setText("It works")
//                snackbar.show()







//                view.snack("This is my snack")
//                view.snack("This snack is short", Snackbar.LENGTH_SHORT)

//                view.snack("Snack message") {
//                    action("Action") { toast("Action clicked") }
//                }

*/


            }

        } catch (e: JSONException) {
            printToErrorLog_10("ActivityMain", "runOnUiThread try")
        }

    }




    fun processMOCKReturn(response: String){
        displayResults(response)
        println("review meh2 utils class amd move all to here")
//    review meh2 utils class amd move all to here
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
                        .placeholder(R.mipmap.ic_failed_to_load_image)
                        .error(R.mipmap.ic_failed_to_load_image)
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
















//    /////////// prior fetchJSON meh2
//
//    // make sure the internet is turned on on the device
//    private fun getJSON_Data_String(): String {
//        var vReturn_String = ""
//
//        try {
//            val httpClient = DefaultHttpClient()
//            // used for html form POST requests
//            //            HttpPost httpPost = new HttpPost( SERVER_URL ) ;
//            // used for html form GET  requests
//            val httpGet = HttpGet(mURL)
//
//            // used for html form POST requests
//            //            HttpResponse response = httpClient.execute ( httpPost ) ;
//            val response = httpClient.execute(httpGet)
//            val statusLine = response.getStatusLine()
//
//            if (statusLine.getStatusCode() === 200) {
//                val entity = response.getEntity()
//                val content = entity.getContent()
//                //mReturn_Reader = new InputStreamReader( content ) ;
//
//                val vBuffer_Reader = BufferedReader(InputStreamReader(content))
//                var vString = ""
//
//                while ((vString = vBuffer_Reader.readLine()) != null) {
//                    vReturn_String = vString
//                }  // while
//
//            } else {
//                printToLog_10(TAG, "error: Server responded with status code: " + statusLine.getStatusCode())
//            } // if
//
////        } catch (e: ClientProtocolException) {
////            e.printStackTrace()
//        } catch (e: IllegalStateException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        // try
//
//        return vReturn_String
//    } //




















    /**
     * [AsyncTask] to perform the network request on a background thread, and then
     * update the UI with the first mehrequest in the response.
     */
//    inner class fetchJSONAsyncTask2 : AsyncTask<URL, Void, ModelMeh>() {
    private inner class fetchJSONAsyncTask2 : AsyncTask<URL, Void, String>() {

        //        override fun doInBackground(vararg urls: URL): ModelMeh {
        override fun doInBackground(vararg urls: URL): String{
            val uString = fetchJSON_AsyncTask()
            println("555aaa  uString  = " + uString)
            return uString
        }

        /**
         * Update the screen with the given mehrequest (which was the result of the
         * [fetchJSONAsyncTask2 ]).
         */
//        override fun onPostExecute(mehrequest: ModelMeh?) {
        override fun onPostExecute(mehrequest: String?) {
            if (mehrequest == null) {
                return
            }
//            updateUi(mehrequest)
        }

        //    /////////// prior fetchJSON udacitymeh2
        @Throws(IOException::class)
        fun fetchJSON_AsyncTask(): String {
            // jsonURL
            var jsonResponse = ""
            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null

            try {
                var siteURL = createUrl2(jsonURL)

                urlConnection = siteURL?.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                urlConnection.connectTimeout = 15000
                urlConnection.connect()

                inputStream = urlConnection.inputStream
                jsonResponse = readFromStream(inputStream)
                println("666ccc    jsonResponse = " + jsonResponse )
            } catch (e: IOException) {
                //
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }

                if (inputStream != null) {
                    inputStream.close()
                }
            }
            return jsonResponse
        }


        fun createUrl2(stringUrl: String): URL? {
            var url: URL? = null
            try {
                url = URL(stringUrl)
            } catch (exception: MalformedURLException) {
                Log.e("createUrl", "Error with creating URL", exception)
                return null
            }

            return url
        }

        /**
         * Convert the [InputStream] into a String which contains the
         * whole JSON response from the server.
         */
        @Throws(IOException::class)
        fun readFromStream(inputStream: InputStream?): String {
            val output = StringBuilder()
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
                val reader = BufferedReader(inputStreamReader)
                var line = reader.readLine()
                while (line != null) {
                    output.append(line)
                    line = reader.readLine()
                }
            }
            return output.toString()
        }








    } // async task


} // class