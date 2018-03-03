package com.keyontech.meh3

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.gson.GsonBuilder
import com.keyontech.meh3.adapters.AdapterViewPagerActivityMain
import kotlinx.android.synthetic.main.content_activity_main.*
import kotlinx.android.synthetic.main.activity_main_v2_nav_drawer.*
import kotlinx.android.synthetic.main.activity_main_v2_nav_drawer_include_content.*
import okhttp3.*
import org.json.JSONException
import java.io.IOException
import android.graphics.Color
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import com.keyontech.meh3.Activities.ActivityAbout
import com.keyontech.meh3.Activities.ActivityMehPoll
import com.keyontech.meh3.Activities.ActivityMehVideo
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.View
import com.keyontech.meh3.Activities.ActivityGoToSite
import com.keyontech.meh3.Models.JSONUrL
import com.keyontech.meh3.Models.ModelMeh
import com.keyontech.meh3.utils.*
import com.keyontech.meh3.viewpager1.*
import kotlinx.android.synthetic.main.nav_drawer_layout.*
import java.util.*
import kotlin.system.exitProcess

class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    /*** api request url */
    var jsonURL = ""
    /*** api response string */
    var jsonResponse = ""

    var mehVideoLink = ""

    // define View Pager
    private lateinit var adapterActivityMain: AdapterViewPagerActivityMain

//    var navBackgroundColor = 0




    companion object {
        var mehDealUrl: String = ""
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_v2_nav_drawer)
        setSupportActionBar(toolbarNavDrawer)

// setup Nav Drawer
        var toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbarNavDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
                /*** dont show nav bar on start */
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
                        .edit()
                        .putBoolean(PREF_KEY_SHOW_NAV_DRAWER_ONSTART, false)
                        .apply()
            }
        }

        fabbuttonNavDrawer.visibility = View.VISIBLE
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
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

        /*** used by notification action button to cancel notification */
        cancelNotification(this, intent)
        /*** start job scheduler */
        if (!isNotificationJobScheduled(this))
            scheduleNotificationJob(this)




// get url from file
// fetch data
        var gson = GsonBuilder().serializeNulls().create() // include null opjects when null
        var urlFile = loadJsonFromFile("url.json", this)
        var jsonOutput = gson.fromJson( urlFile , JSONUrL::class.java )
        jsonURL = jsonOutput.mehurl

        /*** remove title ba text */
        setTitle("")

        /*** call json service */
        fetchJSON()

        /*** fab button */
        fabbuttonNavDrawer.setOnClickListener { view ->
//            setTitle("")

//            fetchJSON()
//            fetchMockInterface()

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            var jsonResponse = sharedPreferences.getString(PREF_KEY_MEH_RESPONSE_STRING, "")
            println("sharedPreferences  :  jsonResponse = " + jsonResponse)

            goToURL(mehDealUrl)
        }
    }

    fun goToURL(pURL: String) {
        println("pUrl = " + pURL)
        var intent = Intent(baseContext, ActivityGoToSite::class.java)
        intent.putExtra(ACT_EXTRA_GO_TO_SITE_URL, pURL)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        /*** Handle navigation view item clicks here. */
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



    fun fetchJSON() {
        var request = Request.Builder().url( jsonURL ).build()
        var client = OkHttpClient()

        /*** had to enqueue because you cannot execute in the main method needs a thread to do so */
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                var responseBody = response?.body()?.string()
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
        try {
            if (response != null && response.isNotEmpty()) {
                var gson = GsonBuilder().serializeNulls().create()
                var modelMeh = gson.fromJson(response, ModelMeh::class.java)

                /*** setup video activity */
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

                /*** set site URL */
                if (modelMeh.deal != null) {
                    /*** set main activity body text */
    //                setTitle(priceLowtoHigh(modelMeh.deal.title))
                    textView_content_activity_main_card_view_1.text = modelMeh.deal.title
                    textView_content_activity_main_card_view_2.text = modelMeh.deal.features
                    textView_content_activity_main_card_view_3.text = modelMeh.deal.specifications

                    if ((modelMeh.deal.items != null) && (modelMeh.deal.items[0].condition != "" && modelMeh.deal.items[0].condition != null)) {
                        textView_content_activity_main_card_view_4.text = modelMeh.deal.items[0].condition + " - " + priceLowtoHigh(modelMeh.deal)
                    } else {
                        textView_content_activity_main_card_view_4.text = priceLowtoHigh(modelMeh.deal)
                    }

                    /*** set the color scheme  --- START */
                    if (modelMeh.deal.theme != null) {
                        if ((modelMeh.deal.theme.accentColor != "" || modelMeh.deal.theme.accentColor != null) || (modelMeh.deal.theme.backgroundColor != "" || modelMeh.deal.theme.backgroundColor != null)) {
                            window.statusBarColor = Color.parseColor(modelMeh.deal.theme.accentColor)
                            window.navigationBarColor = Color.parseColor(modelMeh.deal.theme.accentColor)

                            try {
                                toolbarNavDrawer.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))

                                // set the custom color for the tabs
                                viewPager_NavDrawer.setBackgroundColor(Color.parseColor(modelMeh.deal.theme.backgroundColor))
                                //                    mViewPager_Tabs.setSelectedIndicatorColors(Color.parseColor(modelMeh.deal.theme.accentColor ) );
                            } catch (e: Exception) {
//                                printToErrorLog_10("ActivityMain", "runOnUiThread")
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

                    /*** set fab action button link */
                    if (modelMeh.deal.url != null && modelMeh.deal.url.isNotEmpty()) {
                        mehDealUrl = modelMeh.deal.url
                    } else {
                        mehDealUrl = ""
                        println("set Deal Url here")
                    }

                    /*** save string to preferences */
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .edit()
                            .putString(PREF_KEY_MEH_RESPONSE_STRING, response)
                            .apply()

                    /*** set notification large image */
                    if (modelMeh.deal.photos != null) {
//                        if (modelMeh.deal.photos[0].isNotEmpty()) {
//                            mehNotificationLargePhoto = modelMeh.deal.photos[0]
//                        } else {
//                            mehNotificationLargePhoto = ""
//                            println("set default large photo image here")
//                        }

                        /*** set photos viewPager */
                        adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, modelMeh.deal.photos)
                        viewPager_NavDrawer.offscreenPageLimit = 3
                        viewPager_NavDrawer.adapter = adapterActivityMain

                        /*** set custom swipe animations */
                        val randomNumberCreator = Random()
                        val randomNumber = randomNumberCreator.nextInt(3)

                        when (randomNumber) {
                            0 -> viewPager_NavDrawer.setPageTransformer(false, DepthViewPagerPageTransform())
                            1 -> viewPager_NavDrawer.setPageTransformer(false, TopRightToBottomLeftViewPagerPageTransform())
                            else -> viewPager_NavDrawer.setPageTransformer(false, ParallaxViewPagerPageTransform())
                        }

                        /*** setup viewPager indicator buttons */
                        tab_layout_viewpager_indicator_dots_NavDrawer.setupWithViewPager(viewPager_NavDrawer, true)
                    } else {
                        println("set view pager to null repsonse image")
                    }
                } else {
                    val failedToLoadPhoto : ArrayList<String> = ArrayList()
                    failedToLoadPhoto .add("")

                    /*** set photos viewPager */
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

                Snackbar.make(
                        findViewById(android.R.id.content)
                        , "Unable to fetch data please check internet connection"
                        , Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry") {
                            fetchJSON()
                            printToSnackbar(
                                    findViewById(android.R.id.content)
                                    , "Attempting to fetch data!"
                                    , Snackbar.LENGTH_SHORT
                            )
//                            Snackbar.make(
//                                    findViewById(android.R.id.content)
//                                    , "Attempting to fetch data!"
//                                    , Snackbar.LENGTH_SHORT).show()
                        }.show()

            }

        } catch (e: JSONException) {
//            printToErrorLog_10("ActivityMain", "runOnUiThread try")
        }

    }


    fun processMOCKReturn(response: String){
        displayResults(response)
        println("review meh2 utils class amd move all to here")
    }

}