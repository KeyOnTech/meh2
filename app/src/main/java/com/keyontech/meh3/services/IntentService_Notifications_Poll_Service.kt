package com.keyontech.meh3.services

import android.app.*
import android.content.Intent
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Handler
import android.os.HandlerThread
import android.preference.PreferenceManager
import android.util.Log
import com.example.jonesq.meh3.Models.JSONUrL
import com.example.jonesq.meh3.Models.ModelMeh
import com.example.jonesq.meh3.utils.*
import com.google.gson.GsonBuilder
import com.keyontech.meh3.R
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
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
        var mehDealUrl: String = ""

        // pg 473
            fun setServiceAlarm(pContext: Context, pAlarmIsOn: Boolean) {
                var vIntent = Intent(pContext, IntentService_Notifications_Poll_Service::class.java)
                var vPendingIntent = PendingIntent.getService(pContext, 0, vIntent, 0)

                var vAlarmManager = pContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                if (pAlarmIsOn) {
                    // how to...
                    // https://developer.android.com/training/scheduling/alarms.html
                    // Set the alarm to start at approximately 11:05 p.m.
                    var pCalendar = Calendar.getInstance()
                    pCalendar.timeInMillis = System.currentTimeMillis()

//                        // USE FOR TESTING faster timer for testing notifications --- start
//                        // reset hour, minutes, seconds and millis
//                        pCalendar.set(Calendar.SECOND, 45)
//                        // USE FOR TESTING faster timer for testing notifications --- end

                    // use for live --- start
                    // reset hour, minutes, seconds and millis
                    pCalendar.set(Calendar.HOUR_OF_DAY, 23)  // 21 = 11pm / 0  -  midnight
                    pCalendar.set(Calendar.MINUTE, 4) // 04   -  00:04
                    pCalendar.set(Calendar.SECOND, 0) // 00
                    pCalendar.set(Calendar.MILLISECOND, 0)
                    // use for live --- start


//                        // USE FOR TESTING faster timer for testing notifications --- start
//                        // run at so many seconds over and over
//                        vAlarmManager.setRepeating(
//                                AlarmManager.RTC
//                                , System.currentTimeMillis() // start time for polling
//                                , TEST_NOTIFICATION_POLL_INTERVAL.toLong() // run every so many seconds
//                                , vPendingIntent
//                        )
//                        // USE FOR TESTING faster timer for testing notifications --- end


                    // USE FOR LIVE notifications --- START
                    vAlarmManager.setRepeating(
                            AlarmManager.RTC
                            , pCalendar.timeInMillis // AT time above
                            , AlarmManager.INTERVAL_DAY // DAILY
                            , vPendingIntent
                    )
                    // USE FOR LIVE notifications --- END
                } else {
                    vAlarmManager.cancel(vPendingIntent)
                    vPendingIntent.cancel()
                }


                // save to preferences if the alarm is on or off for startup
                PreferenceManager.getDefaultSharedPreferences(pContext)
                        .edit()
                        .putBoolean(PREF_EXTRA_NOTIFICATION_IS_ALARM_ON, pAlarmIsOn)
                        .apply()

            } // isServiceAlarm( Context pContext )


            // pg 475
            fun isServiceAlarmOn(pContext: Context): Boolean {
                var vIntent = Intent(pContext, IntentService_Notifications_Poll_Service::class.java)
                var vPendingIntent = PendingIntent.getService(pContext, 0, vIntent, PendingIntent.FLAG_NO_CREATE)
                return vPendingIntent != null
            } // isServiceAlarmOn( Context pContext , boolean vIsOn )

    } // companion object




    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            try {
                // fetch JSON
                // parse JSON return
                // show notification here
//                println("IntentService_Notifications_Poll_Service - fetch JSON")
                fetchJSON()
                // Kick off an {@link AsyncTask} to perform the network request
                val task_TsunamiAsyncTask1 = fetchJSONAsyncTask()
                task_TsunamiAsyncTask1.execute()

                // send broadcast
//                println("onHandleIntent - send broadcast1 - send broadcast")
                sendBroadcast(Intent(BROADDCAST_EXTRA_ACTION_SHOW_NOTIFICATION))
            }  catch (e:Exception){
                println("error in IntentService_Notifications_Poll_Service " + e.message )
            }
        }
    }









    fun fetchJSON() {
        try {
            var gson = GsonBuilder().serializeNulls().create()
            // get url from file
            var urlFile = loadJsonFromFile("url.json", this)
            var jsonOutput = gson.fromJson(urlFile, JSONUrL::class.java)
            jsonURL = jsonOutput.url

            var request = Request.Builder().url(jsonURL).build()
            var client = OkHttpClient()

            // fetch data
            /*** had to enqueue because you cannot execute in the main method needs a thread to do so */
            //        client.newCall( request ).execute()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    var responseBody = response?.body()?.string()
                    jsonResponse = responseBody.toString()
                    println("444ggg    jsonResponse = " + jsonResponse )
                    processReturn(jsonResponse)
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    println("Fetch data service call failed - Error: " + e.toString())
                }
            })
        } catch (e: JSONException) {
            printToErrorLog_10("IntentService_Notifications_Poll_Service Error", "fetchJSON try")
        }
    }

    fun processReturn(response: String){
        var gson = GsonBuilder().serializeNulls().create()
        var modelMeh = gson.fromJson( response , ModelMeh::class.java )

        // save string to preferences
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putString(PREF_KEY_MEH_RESPONSE_STRING, response)
            .apply()


        // set site URL
        if (modelMeh.deal != null )
        {
            // set site URL
            // set fab action button link
            if(modelMeh.deal.url != null && modelMeh.deal.url.isNotEmpty()) {
                mehDealUrl = modelMeh.deal.url
            }else{
                mehDealUrl = ""
                println("set Deal Url here")
            }

            // set notification large image
            if (modelMeh.deal.photos != null) {
                if(modelMeh.deal.photos[0].isNotEmpty()) {
                    mehNotificationLargePhoto = modelMeh.deal.photos[0]
                }else {
                    mehNotificationLargePhoto= ""
                    println("set default large photo image here")
                }

                if ((modelMeh.deal.items != null) && (modelMeh.deal.items[0].condition != "" && modelMeh.deal.items[0].condition != null)) {
                    // display notification
                    createNotification(
                            this
                            ,"Meh 1 fetchJSON"
                            ,modelMeh.deal.title + "-1-OkHttpClient"
                            ,modelMeh.deal.items[0].condition + " - " + priceLowtoHigh(modelMeh.deal)
                            , R.drawable.logo_32_x_32_2
                            , R.drawable.logo_32_x_32_2
                            ,mehNotificationLargePhoto
                            , R.drawable.logo_32_x_32_2
                    )
                }else{
                    println("set view pager to null repsonse image")
                }
            }else{
                println("set view pager to null repsonse image")
            }

        }else{
            println("set all content boxes to to null repsonses")
        }

    }

    fun createNotification( pContext: Context, tickerText: String = "", notificationTitle: String = "", notificationText: String, showactionRightButtonIcon: Int, showactionLeftButtonIcon: Int, largebitmapImageURL : String, smallIcon : Int) {
        var handlerThread = HandlerThread("IntentService_Notifications_Poll_Service_createNotification_handlerThread")
        handlerThread.start()

        var handler = Handler(handlerThread.looper)
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
                        ,mehDealUrl
                        ,NOTIFICATION_ID
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






















    /**
     * [AsyncTask] to perform the network request on a background thread, and then
     * update the UI with the first mehrequest in the response.
     */
//    inner class fetchJSONAsyncTask : AsyncTask<URL, Void, ModelMeh>() {
    inner class fetchJSONAsyncTask : AsyncTask<URL, Void, String>() {

        //        override fun doInBackground(vararg urls: URL): ModelMeh {
        override fun doInBackground(vararg urls: URL): String{
            val uString = fetchJSON_AsyncTask()
            return uString

//            // Create URL object
//            val url = createUrl(jsonURL)
//
//            // Perform HTTP request to the URL and receive a JSON response back
//            var jsonResponse = ""
//            try {
//                jsonResponse = makeHttpRequest(url)
//            } catch (e: IOException) {
//                // TODO Handle the IOException
//            }
//
//            // Extract relevant fields from the JSON response and create an {@link ModelMeh} object
//
//            // Return the {@link ModelMeh} object as the result fo the {@link TsunamiAsyncTask}
//            return extractFeatureFromJson(jsonResponse)

        }

        /**
         * Update the screen with the given mehrequest (which was the result of the
         * [TsunamiAsyncTask]).
         */
//        override fun onPostExecute(mehrequest: ModelMeh?) {
        override fun onPostExecute(mehrequest: String?) {
            if (mehrequest == null) {
                return
            }
//            updateUi(mehrequest)
        }






        ////////////////
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
                processReturn2(jsonResponse)
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










        fun processReturn2(response: String){
            var gson = GsonBuilder().serializeNulls().create()
            var modelMeh = gson.fromJson( response , ModelMeh::class.java )

            // save string to preferences
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    .edit()
                    .putString(PREF_KEY_MEH_RESPONSE_STRING, response)
                    .apply()

            // set site URL
            if (modelMeh.deal != null )
            {
                // set site URL
                // set fab action button link
                if(modelMeh.deal.url != null && modelMeh.deal.url.isNotEmpty()) {
                    mehDealUrl = modelMeh.deal.url
                }else{
                    mehDealUrl = ""
                    println("set Deal Url here")
                }


                // set notification large image
                if (modelMeh.deal.photos != null) {
                    if(modelMeh.deal.photos[0].isNotEmpty()) {
                        mehNotificationLargePhoto = modelMeh.deal.photos[0]
                    }else {
                        mehNotificationLargePhoto= ""
                        println("set default large photo image here")
                    }

                    if ((modelMeh.deal.items != null) && (modelMeh.deal.items[0].condition != "" && modelMeh.deal.items[0].condition != null)) {
                        // display notification
                        createNotification2(
                                applicationContext
                                , "Meh 2 async task"
                                , modelMeh.deal.title + "-2-Async_Task"
                                , modelMeh.deal.items[0].condition + " - " + priceLowtoHigh(modelMeh.deal)
                                , R.drawable.logo_32_x_32_2
                                , R.drawable.logo_32_x_32_2
                                , mehNotificationLargePhoto
                                , R.drawable.logo_32_x_32_2
                        )
                    }
                }else{
                    println("set view pager to null repsonse image")
                }

            }else{
                println("set all content boxes to to null repsonses")
            }

        }


        fun createNotification2( pContext: Context, tickerText: String = "", notificationTitle: String = "", notificationText: String, showactionRightButtonIcon: Int, showactionLeftButtonIcon: Int, largebitmapImageURL : String, smallIcon : Int) {
            var handlerThread = HandlerThread("IntentService_Notifications_Poll_Service_createNotification2_handlerThread")
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
                            ,mehDealUrl
                            ,NOTIFICATION_ID + 1
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







        ////////////////

//        /**
//         * Returns new URL object from the given string URL.
//         */
//        fun createUrl(stringUrl: String): URL? {
//            var url: URL? = null
//            try {
//                url = URL(stringUrl)
//            } catch (exception: MalformedURLException) {
//                Log.e("createUrl", "Error with creating URL", exception)
//                return null
//            }
//
//            return url
//        }
//
//        /**
//         * Make an HTTP request to the given URL and return a String as the response.
//         */
//        @Throws(IOException::class)
//        fun makeHttpRequest(url: URL): String {
//            var jsonResponse = ""
//            var urlConnection: HttpURLConnection? = null
//            var inputStream: InputStream? = null
//            try {
//                urlConnection = url.openConnection() as HttpURLConnection
//                urlConnection.requestMethod = "GET"
//                urlConnection.readTimeout = 10000
//                urlConnection.connectTimeout = 15000
//                urlConnection.connect()
//                inputStream = urlConnection.inputStream
//                jsonResponse = readFromStream(inputStream)
//            } catch (e: IOException) {
//                // TODO: Handle the exception
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect()
//                }
//                if (inputStream != null) {
//                    // function must handle java.io.IOException here
//                    inputStream.close()
//                }
//            }
//            return jsonResponse
//        }
//
//        /**
//         * Convert the [InputStream] into a String which contains the
//         * whole JSON response from the server.
//         */
//        @Throws(IOException::class)
//        fun readFromStream(inputStream: InputStream?): String {
//            val output = StringBuilder()
//            if (inputStream != null) {
//                val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
//                val reader = BufferedReader(inputStreamReader)
//                var line = reader.readLine()
//                while (line != null) {
//                    output.append(line)
//                    line = reader.readLine()
//                }
//            }
//            return output.toString()
//        }
//
//        /**
//         * Return an [ModelMeh] object by parsing out information
//         * about the first mehrequest from the input mehrequestJSON string.
//         */
//        fun extractFeatureFromJson(mehrequestJSON: String): ModelMeh? {
//            try {
//                val baseJsonResponse = JSONObject(mehrequestJSON)
//                val featureArray = baseJsonResponse.getJSONArray("features")
//
//                // If there are results in the features array
//                if (featureArray.length() > 0) {
//                    // Extract out the first feature (which is an mehrequest)
//                    val firstFeature = featureArray.getJSONObject(0)
//                    val properties = firstFeature.getJSONObject("properties")
//
//                    // Extract out the title, time, and tsunami values
//                    val title = properties.getString("title")
//                    val time = properties.getLong("time")
//                    val tsunamiAlert = properties.getInt("tsunami")
//
//                    // Create a new {@link ModelMeh} object
//                    return ModelMeh(title, time, tsunamiAlert)
//                }
//            } catch (e: JSONException) {
//                Log.e("extractFeatureFromJson", "Problem parsing the mehrequest JSON results", e)
//            }
//
//            return null
//        }


    } // async task




} // class