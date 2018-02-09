package com.keyontech.meh3.services

import android.app.*
import android.content.Intent
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.preference.PreferenceManager
import com.example.jonesq.meh3.Models.JSONUrL
import com.example.jonesq.meh3.Models.ModelMeh
import com.example.jonesq.meh3.utils.*
import com.google.gson.GsonBuilder
import com.keyontech.meh3.R
import com.squareup.picasso.Picasso
import okhttp3.*
import java.io.IOException
import java.util.*



/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class IntentService_Notifications_Poll_Service : IntentService("IntentService_Notifications_Poll_Service") {
    // used for boot up to see if the alarm is set to on or off

    /*** this is used for the notification large image */
    var mehNotificationLargePhoto = ""
    /*** api request url */
    var jsonURL = ""
    /*** api response string */
    var jsonResponse = ""





        companion object {
                // pg 473
                fun setServiceAlarm(pContext: Context, pAlarmIsOn: Boolean) {
                    println( "444  IntentService_Notifications_Poll_Service - setServiceAlarm - pAlarmIsOn =  " + pAlarmIsOn )
                    // how to...
                    // https://developer.android.com/training/scheduling/alarms.html
                    // Set the alarm to start at approximately 11:05 p.m.
                    var pCalendar = Calendar.getInstance()
                    pCalendar.timeInMillis = System.currentTimeMillis()

                    // reset hour, minutes, seconds and millis
                    pCalendar.set(Calendar.HOUR_OF_DAY, 23)  // 11pm
                    //        pCalendar.set( Calendar.HOUR_OF_DAY, 0 );  // 0  -  midnight
                    pCalendar.set(Calendar.MINUTE, 4) // 04   -  :04
                    pCalendar.set(Calendar.SECOND, 0)
                    pCalendar.set(Calendar.MILLISECOND, 0)

                    var vIntent = Intent(pContext, IntentService_Notifications_Poll_Service::class.java)
                    var vPendingIntent = PendingIntent.getService(pContext, 0, vIntent, 0)

        //        var vAlarmManager = pContext.getSystemService( pContext.ALARM_SERVICE) as AlarmManager
                    var vAlarmManager = pContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    if (pAlarmIsOn) {
//                        println( "555  IntentService_Notifications_Poll_Service - setServiceAlarm - pAlarmIsOn =  " + pAlarmIsOn )

        //                // USE FOR TESTING faster timer for testing notifications --- start
        //                // run at so many seconds over and over
        //                vAlarmManager.setRepeating(
        //                        AlarmManager.RTC
        //                        , System.currentTimeMillis() // start time for polling
        //                        , EXTRA_NOTIFICATION_POLL_INTERVAL.toLong() // run every so many seconds
        //                        , vPendingIntent
        //                )
        //                // USE FOR TESTING faster timer for testing notifications --- end

                        // USE FOR LIVE notifications --- START
                        vAlarmManager.setRepeating(
                                AlarmManager.RTC
                                , pCalendar.timeInMillis // AT time above
                                , AlarmManager.INTERVAL_DAY // DAILY
                                , vPendingIntent
                        )
                        // USE FOR LIVE notifications --- END
                    } else {
                        println( "666  IntentService_Notifications_Poll_Service - setServiceAlarm - pAlarmIsOn =  " + pAlarmIsOn )

                        vAlarmManager.cancel(vPendingIntent)
                        vPendingIntent.cancel()
                    } // if


        // save to preferences if the alarm is on or off for startup
        //            println("888  save to preferences   pAlarmIsOn  =  "  + pAlarmIsOn )
                    PreferenceManager.getDefaultSharedPreferences(pContext)
                            .edit()
                            .putBoolean(EXTRA_NOTIFICATION_IS_ALARM_ON, pAlarmIsOn)
                            .apply()

                } // isServiceAlarm( Context pContext )

                // pg 475
                fun isServiceAlarmOn(pContext: Context): Boolean {
        //            println("111  isServiceAlarmOn " )

                    var vIntent = Intent(pContext, IntentService_Notifications_Poll_Service::class.java)
                    var vPendingIntent = PendingIntent.getService(pContext, 0, vIntent, PendingIntent.FLAG_NO_CREATE)
                    return vPendingIntent != null
                } // isServiceAlarmOn( Context pContext , boolean vIsOn )

        } // companion



    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            try {
                // fetch JSON
                // parse JSON return
                // show notification here
//                println("IntentService_Notifications_Poll_Service - fetch JSON")
                fetchJSON()

                // send broadcast
//                println("onHandleIntent - send broadcast1 - send broadcast")
                sendBroadcast( Intent(EXTRA_ACTION_SHOW_NOTIFICATION))
            }  catch (e:Exception){
                println("error in IntentService_Notifications_Poll_Service " + e.message )
            }
        }
    }




    fun fetchJSON() {
// get url from file
// fetch data
        var gson = GsonBuilder().create()
        var urlFile = loadJsonFromFile("url.json", this)
        var jsonOutput = gson.fromJson( urlFile , JSONUrL::class.java )
        jsonURL = jsonOutput.url

        var request = Request.Builder().url( jsonURL ).build()
        var client = OkHttpClient()

        /*** had to enqueue because you cannot execute in the main method needs a thread to do so */
//        client.newCall( request ).execute()
        client.newCall( request ).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
//                println("111aaa LOAD : live data")

                var responseBody = response?.body()?.string()
                println( "111bbb  fetchJSON - onResponse - body - " + responseBody )
                jsonResponse = responseBody.toString()
                processReturn(jsonResponse)
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("fetchJSON - failed to execute request - error: " + e.toString() )
            }
        })
    }

    fun processReturn(response: String){
        var gson = GsonBuilder().create()
        var modelMeh = gson.fromJson( response , ModelMeh::class.java )

        // save string to preferences
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putString(KEY_MEH_RESPONSE_STRING, response)
            .apply()

        // set notification large image
        mehNotificationLargePhoto = modelMeh.deal.photos[0]

        // display notification
        createNotification(
                this
                ,"Meh"
                ,modelMeh.deal.title
                ,priceLowtoHigh(modelMeh.deal)
                , R.drawable.logo_32_x_32_2
                , R.drawable.logo_32_x_32_2
                ,mehNotificationLargePhoto
                , R.drawable.logo_32_x_32_2
        )
    }


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

}