package com.keyontech.meh3

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
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
import android.graphics.BitmapFactory
import com.example.jonesq.meh3.Models.ModelMehDeal









import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import com.example.jonesq.meh3.Models.*
import com.example.jonesq.meh3.utils.KEY_MEH_RESPONSE_STRING
import com.example.jonesq.meh3.utils.KEY_MEH_VIDEO_LINK
import com.keyontech.meh3.Activities.ActivityAbout
import com.keyontech.meh3.Activities.ActivityMehPoll
import com.keyontech.meh3.Activities.ActivityMehVideo
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import com.example.jonesq.meh3.utils.KEY_PHOTO_URI
import android.R.attr.smallIcon
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.support.v4.app.NotificationCompat


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

    /*** this is used for the notification large image */
    var mehNotificationLargePhoto = ""
    var NOTIFICATION_ID = 333


//    var mehPoll = ModelMehPoll.Companion
//    var mehPoll = {}
//    var mehPoll: ModelMehPoll
//    var mehPoll = ModelMehPoll()


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



            ///////
/*


//            val pShow_Large_Icon_Bitmap

            Picasso.with(this)
                    .load(mehNotificationLargePhoto)

                    .resize(32, 32)
                    .placeholder(R.drawable.logo_32_x_32_2)
//                    .error(R.drawable.logo_32_x_32_2)

//                    .get()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                            mRemoteViews.setImageViewBitmap(R.id.myImage,bitmap);



                            // create notification - start
                            val intent = Intent()
                            val pendingIntent = PendingIntent.getActivity(this@ActivityMain,0,intent,0)
                            val notification = Notification.Builder(this@ActivityMain)
                                    .setTicker("")
                                    .setContentTitle("")
                                    .setContentText("")
                                    .setSmallIcon(R.drawable.notification_bg)
                                .setLargeIcon(Bitmap)

                                    .setContentIntent(pendingIntent).notification

                            notification.flags = Notification.FLAG_AUTO_CANCEL
                            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.notify(0, notification)


                            // crate notification - end

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            //do something when loading failed
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            //do something while loading
                         }
                    })








//
//            Handler uiHandler = new Handler(Looper.getMainLooper())
//            uiHandler.post(() -> {
//                val pShow_Large_Icon_Bitmap = Picasso
//                        .with(this)
//                        .load(mehNotificationLargePhoto)
//        //                                                .load( R.drawable.logo_32_x_32_2 )
//        ////                    .load(pMeh_API_v1.getDeal_Details().getmPhotos().get(0))
//        ////                    .load(R.drawable.logo_32_x_32_2)
//                        .resize(32, 32)
//                        .placeholder(R.drawable.logo_32_x_32_2)
//                        .error(R.drawable.logo_32_x_32_2)
//                        .get()
//
////            Picasso.with(this)
////                        .load(mehNotificationLargePhoto)
////                        .into(remoteViews, viewId, new int[]{widgetId});
//
//            });
//
//            Thread {
//
//                println("Thread start ")
//                    val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//                    val builder = NotificationCompat.Builder(this)
//                            .setContentTitle(title)
//                            .setContentText("abc")
//                            .setSmallIcon(smallIcon)
//                            .setContentIntent(
//                                    PendingIntent.getActivity(
//                                            this,
//                                            0,
//                                            Intent(this, ActivityMain::class.java),
//                                            PendingIntent.FLAG_UPDATE_CURRENT)
//                            )
//                            //here comes to load image by Picasso
//                            //it should be inside try block
//                            .setLargeIcon(Picasso.with(this).load("URL_TO_LOAD_LARGE_ICON").get())
//                            //BigPicture Style
//                            .setStyle(NotificationCompat.BigPictureStyle()
//                                    //This one is same as large icon but it wont show when its expanded that's why we again setting
//                                    .bigLargeIcon(Picasso.with(this).load(mehNotificationLargePhoto).get())
//                                    //This is Big Banner image
//                                    .bigPicture(Picasso.with(this).load(mehNotificationLargePhoto).get())
//                                    //When Notification expanded title and content text
//                                    .setBigContentTitle(title)
//                                    .setSummaryText("abcDEF")
//                            )
//
//                return@Thread
//
//            }.run()

            //////////






            // notification large icon
            val pBitmap_Map = BitmapFactory.decodeResource(resources, R.drawable.logo_512_x_512_2)
            val pBitmap_MapScaled = Bitmap.createScaledBitmap(pBitmap_Map, 96, 96, true)
            val pShow_Large_Icon_Bitmap = Picasso
                    .with(this)
                    .load(mehNotificationLargePhoto)
//                                                .load( R.drawable.logo_32_x_32_2 )
////                    .load(pMeh_API_v1.getDeal_Details().getmPhotos().get(0))
////                    .load(R.drawable.logo_32_x_32_2)
                    .resize(32, 32)
                    .placeholder(R.drawable.logo_32_x_32_2)
                    .error(R.drawable.logo_32_x_32_2)
                    .get()








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

*/
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
            R.id.nav_bar_poll -> {
                val intent = Intent(baseContext, ActivityMehPoll::class.java)
                intent.putExtra(KEY_MEH_RESPONSE_STRING, jsonResponse)
                startActivity(intent)
            }
            R.id.nav_bar_video-> {
                val intent = Intent(baseContext, ActivityMehVideo::class.java)
                intent.putExtra(KEY_MEH_VIDEO_LINK, mehVideoLink)
                startActivity(intent)
            }
            R.id.nav_bar_about-> {
                val intent = Intent(baseContext, ActivityAbout::class.java)
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
        println("ActiviyMain - onCreate - attempting to fetch JSON")
        println("url " + jsonURL)

        val request = Request.Builder().url( jsonURL ).build()
        val client = OkHttpClient()

        /*** had to enqueue because you cannot execute in the main method needs a thread to do so */
//        client.newCall( request ).execute()
        client.newCall( request ).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                println("LOAD : live data")

                val responseBody = response?.body()?.string()
                println( "111  fetchJSON - onResponse - body - " + responseBody )
                jsonResponse = responseBody.toString()

                processReturn(jsonResponse)
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("fetchJSON - failed to execute request - error: " + e.toString() )
            }
        })
    } //



















    fun mockInterface() {
        println("Mock Data initiated ")

        try {
            println("LOAD :  MOCK data")
            val mockData = loadJsonFromFile("sample1.json", this)
            jsonResponse = mockData

            processReturn(jsonResponse)
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
    }

    fun priceLowtoHigh(modelMehDeal: ModelMehDeal): String {
        var vMin = modelMehDeal.items[0].price.toInt()
        var vMax = modelMehDeal.items[0].price.toInt()

        for (i in modelMehDeal.items) {
            if (vMin > i.price) vMin = i.price.toInt()
            if (vMax < i.price) vMax = i.price.toInt()
//            print(" \r\n 44401: price " + i.price.toInt() )
        }

        if (vMin != vMax) {
            return "$ $vMin - $ $vMax"
        }else{
            return "$ $vMax"
        }
    }

    fun processReturn(response: String){
        val gson = GsonBuilder().create()
        val modelMeh = gson.fromJson( response , ModelMeh::class.java )


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
            // setup interface
//          setTitle(priceLowtoHigh(modelMeh.deal))

            // set body text
            textView_content_activity_main_card_view_1.text = modelMeh.deal.title
            textView_content_activity_main_card_view_4.text = priceLowtoHigh(modelMeh.deal)
            textView_content_activity_main_card_view_2.text = modelMeh.deal.features
            textView_content_activity_main_card_view_3.text = modelMeh.deal.specifications

            // set video
            mehVideoLink = modelMeh.video.topic.url
//            println( "444bbb  modelMeh.video title = " + modelMeh.video.title )
//            println( "444bbb  modelMeh.video startDate = " + modelMeh.video.startDate )
//            println( "444bbb  modelMeh.video topic = " + modelMeh.video.topic )

            // set notification large image
            mehNotificationLargePhoto = modelMeh.deal.photos[0]

            // set photos viewPager
            adapterActivityMain = AdapterViewPagerActivityMain(supportFragmentManager, modelMeh.deal.photos )
            viewPager_NavDrawer.adapter = adapterActivityMain

            // setup viewPager indicator buttons
            tab_layout_viewpager_indicator_dots_NavDrawer.setupWithViewPager(viewPager_NavDrawer,true)


            // display notification
            createNotification6()
        }
    }






    private fun createNotification6() {
        val handlerThread = HandlerThread("aaa")
        handlerThread.start()

        val handler = Handler(handlerThread.getLooper())
        handler.post(Runnable {
            var notificationLargeBitmap  : Bitmap? = null
            try {
                notificationLargeBitmap  = Picasso
                        .with(this)
                        .load(mehNotificationLargePhoto)
                        .resize(96,96)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .get()




                showNotification(this,"ticker text", "title title", "text text", R.mipmap.ic_launcher,R.mipmap.ic_launcher, notificationLargeBitmap,R.mipmap.ic_launcher)


/* works start
                // setup the values for the notification  --- start
                val pTickerBarText = "Meh.com Update - pTickerBarText "
                val pContentTitle = "pContentTitle "
                val pContentText = "pMeh_API_v1.getDeal_Details().getmItems().get(0).getmCondition()" +
                        " - " + "NumberFormat.getCurrencyInstance().format(pMeh_API_v1.getDeal_Details().getmItems().get(0).getmPrice()))"


                val intent = Intent()
                val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, intent, 0)
                val notification1 = Notification.Builder(this@MainActivity)
                        .setTicker(pTickerBarText)
                        .setContentTitle(pContentTitle)
                        .setContentText(pContentText)

                        .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(notificationLargeBitmap )
                        .setContentIntent(pendingIntent).notification

                notification1.flags = Notification.FLAG_AUTO_CANCEL
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(0, notification1)
 works end  */



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










    //    public static void showNotification( Context vContext , int vCount , String vNotification_TickerTitle , String vNotification_Title , String vNotification_Message , int vShow_Small_Icon_INT , int vShow_Small_Action_Icon_INT   )
    fun showNotification(vContext: Context, vNotification_TickerText: String, vNotification_Title: String, vNotification_Text: String, vShow_Action_Right_Button_Icon_INT: Int, vShow_Action_Left_Button_Icon_INT: Int, vShow_Large_Icon_Bitmap: Bitmap, vShow_Small_Icon_Int: Int) {
        // new --- start
        try {
            // Gets a PendingIntent containing the entire back stack
            // define what activity should appear when the user clicks the notification
            //        Intent vIntentShowActivity = new Intent( vContext , Controller_Activity_MainActivity.class);
            val vIntentShowActivity = Intent()
            // buy --- start
//            val vIntentShowActivity2 = Meh_API_v1.buyThisItem(vContext)
//            val vIntentShowActivity2 = Intent(vContext, this@MainActivity)
            val vIntentShowActivity2 = Intent()
            // buy --- end
            // Android Wear --- start
//            val pIntent_AndroidWear = Intent(vContext, this@MainActivity)
            val pIntent_AndroidWear = Intent()
            // android wear --- end


            // Because clicking the notification opens a new ("special") activity, there's
            // no need to create an artificial back stack.
            val vPendingIntent = PendingIntent.getActivity(
                    vContext,
                    0,
                    vIntentShowActivity,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            // buy --- start
            val vPendingIntent2 = PendingIntent.getActivity(
                    vContext,
                    0,
                    vIntentShowActivity2,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            // buy --- end

            // android wear --- start
            val pPendingIntent_AndroidWear = PendingIntent.getActivity(
                    vContext, 0, pIntent_AndroidWear, PendingIntent.FLAG_UPDATE_CURRENT
            )
            // android wear --- end

            // android wear -- start
            val vNotification_Action = NotificationCompat.Action.Builder(
                    vShow_Small_Icon_Int, vNotification_Title, pPendingIntent_AndroidWear
            ).build()
            // android wear --- end

            // Play sound --- start
            val pNotification_Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            // Play sound --- end


            //  BigPictureStyle --- start
            val pNotification_Big_Picture_Style = NotificationCompat.BigPictureStyle()
            //                    if (expandedIconUrl != null) {
            //                        pNotification_Big_Picture_Style.bigLargeIcon(Picasso.with(context).load(expandedIconUrl).get());
            //                    } else if (expandedIconResId > 0) {
            //                        pNotification_Big_Picture_Style.bigLargeIcon(BitmapFactory.decodeResource(context.getResources(), expandedIconResId));
            //                    } // if
            pNotification_Big_Picture_Style.bigLargeIcon(vShow_Large_Icon_Bitmap)
            pNotification_Big_Picture_Style.bigPicture(vShow_Large_Icon_Bitmap)
            //  BigPictureStyle --- end


            val pNotification_Build = NotificationCompat.Builder(vContext)
                    .setTicker(vNotification_TickerText)  //  vResources.getString( R.string.polling_new_item_title ) )
                    .setContentTitle(vNotification_Title) // vResources.getString( R.string.polling_new_item_title ) )
                    .setContentText(vNotification_Text)
                    .setLargeIcon(vShow_Large_Icon_Bitmap) // pPicasso_Image )
                    .setSmallIcon(vShow_Small_Icon_Int) //   R.drawable.logo_32_x_32_2)
                    .setContentIntent(vPendingIntent)

                    // VIBRATE ETC --- start
                    // ADD PERMISSION TO MANIFEST
                    //      <!-- Used for START Polling Service on StartUp USE -->
                    //      < uses - permission android:name="android.permission.VIBRATE" />
                    .setDefaults(Notification.DEFAULT_ALL)
                    //                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    // VIBRATE ETC --- END

                    // Play sound --- start
                    //            https://www.youtube.com/watch?v=WZX4ovWDzpI
                    .setSound(pNotification_Sound)
                    // Play sound --- end

                    //  BigPictureStyle --- start
                    .setStyle(pNotification_Big_Picture_Style)
                    //  BigPictureStyle --- end

                    // Android Wear --- start
                    .extend(
                            NotificationCompat.WearableExtender()
                                    .addAction(vNotification_Action)
                    )
                    // Android Wear --- end

                    .setAutoCancel(true)

                    .setStyle(pNotification_Big_Picture_Style)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                    .addAction(
                            vShow_Action_Left_Button_Icon_INT, "Details", vPendingIntent)

                    .addAction(
                            vShow_Action_Right_Button_Icon_INT, "Buy", vPendingIntent2) // buy

            val vNotification_Show = pNotification_Build.build()

            val vNotificationManager = vContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            vNotificationManager.notify(NOTIFICATION_ID, vNotification_Show)

        } catch (e: Exception) {
            println("showNotification - error")
        }

    } // showNotification







} // activity
