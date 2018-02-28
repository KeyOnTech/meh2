package com.keyontech.meh3.services

/**
 * Created by kot on 2/26/18.
 */


import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.AsyncTask
import android.os.Handler
import android.os.HandlerThread
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.jonesq.meh3.Models.JSONUrL
import com.example.jonesq.meh3.Models.ModelMeh
import com.example.jonesq.meh3.utils.*
import com.google.gson.GsonBuilder
import com.keyontech.meh3.Models.modelJobExecuterAsyncTaskParams
import com.keyontech.meh3.R
import com.squareup.picasso.Picasso
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kot on 2/23/18.
 * https://stackoverflow.com/questions/44525388/asynctask-in-android-with-kotlin
 *
 *
 *
//    override fun onPreExecute() {
//        super.onPreExecute()
//        // ...
//    }
//
//    override fun onPostExecute(result: String?) {
//        super.onPostExecute(result)
//        // ...
//    }
 */

/*** open added here because all kotlin classes are final this allows you to prevent that
 * https://stackoverflow.com/questions/45168797/kotlin-activity-cannot-be-extended-this-type-is-final-so-it-cannot-be-inherite
 */
//open class MJobExecuter : AsyncTask<Void, Void, String> {
//open class MJobExecuter : AsyncTask<modelJobExecuterAsyncTaskParams, Void, String> {
open class MJobExecuter : AsyncTask<modelJobExecuterAsyncTaskParams, Void, String>() {

    /**
     * [AsyncTask] to perform the network request on a background thread, and then
     * update the UI with the first mehrequest in the response.
     */
//    inner class fetchJSONAsyncTask1 : AsyncTask<URL, Void, ModelMeh>() {
//    inner class fetchJSONAsyncTask1 : AsyncTask<URL, Void, String>() {
    /*** this is used for the notification large image */
    var mehNotificationLargePhoto = ""
    /*** api request url */
    var jsonURL = ""
    /*** api response string */
    var jsonResponse = ""


    /*** public vars */
//    private lateinit var cContext: Context
//    private var mehDealUrl: String = ""

//    constructor(params: modelJobExecuterAsyncTaskParams) {
////        params.cContext
//    }

//    constructor(cContext: Context, mehDealUrl: String) : super() {
//        this.cContext = cContext
//        this.mehDealUrl = mehDealUrl
//    }

    /***
    start an alarm manager
    have that create a job scheduler
    the job scheudler runs when wifi is available
    delete job

    repeat when alarm manager starts the next alart
    make the ID the time stamp in miliseconds

     */


//    override fun doInBackground(vararg params: Void?): String? {
    override fun doInBackground(vararg params: modelJobExecuterAsyncTaskParams?): String? {
        var vContext = params[0]!!.cContext
        var vURL = params[0]!!.dealUrl

        val uString = fetchJSON_AsyncTask(vContext, vURL)

//        showNotificationNow()

        return uString + "MJobExecuter - Background long running task finishes..."
    }

//    private fun showNotificationNow() {
//        try {
//            val todaysDate = SimpleDateFormat("MM-dd-yyyy HH:mm").format(Date())
//            val iTodaysDate = SimpleDateFormat("ss").format(Date())
//            val notificationID = 4441 + Integer.parseInt(iTodaysDate)
//
//            var pNotification_Build = NotificationCompat.Builder(cContext)
//                    .setTicker(todaysDate + " - JS 1 Ticker Text")  //  vResources.getString( R.string.polling_new_item_title ) )
//                    .setContentTitle(todaysDate + " - JS 1 Content Title") // vResources.getString( R.string.polling_new_item_title ) )
//                    .setContentText(todaysDate + " - JS 1 Content Text")
//                    .setSmallIcon(R.mipmap.ic_launcher) //   R.drawable.logo_32_x_32_2)
////                    .setContentIntent(vPendingIntent)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setDefaults(Notification.DEFAULT_ALL)
//
//            var vNotification_Show = pNotification_Build.build()
//            var vNotificationManager =  cContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            vNotificationManager.notify(notificationID, vNotification_Show)
//        } catch (e: Exception) {
//            println("MJobExecuter2  - sendNotification  - error: " + e.message)
//        }
//    }








//        //        override fun doInBackground(vararg urls: URL): ModelMeh {
//        override fun doInBackground(vararg urls: URL): String{
//            val uString = fetchJSON_AsyncTask()
//            return uString
//
////            // Create URL object
////            val url = createUrl(jsonURL)
////
////            // Perform HTTP request to the URL and receive a JSON response back
////            var jsonResponse = ""
////            try {
////                jsonResponse = makeHttpRequest(url)
////            } catch (e: IOException) {
////                // TODO Handle the IOException
////            }
////
////            // Extract relevant fields from the JSON response and create an {@link ModelMeh} object
////
////            // Return the {@link ModelMeh} object as the result fo the {@link fetchJSONAsyncTask1}
////            return extractFeatureFromJson(jsonResponse)
//
//        }

        /**
         * Update the screen with the given mehrequest (which was the result of the
         * [fetchJSONAsyncTask11]).
         */
//        override fun onPostExecute(mehrequest: ModelMeh?) {
//        override fun onPostExecute(mehrequest: String?) {
//            if (mehrequest == null) {
//                return
//            }
//            updateUi(mehrequest)
//        }






        ////////////////
        //    /////////// prior fetchJSON udacitymeh2
        @Throws(IOException::class)
        fun fetchJSON_AsyncTask(pContext: Context, pDealUrl: String = ""): String {
            var gson = GsonBuilder().serializeNulls().create() // include null opjects when null
            var urlFile = loadJsonFromFile("url.json", pContext)
            var jsonOutput = gson.fromJson( urlFile , JSONUrL::class.java )
            var jsonURL = jsonOutput.url

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
                processReturn2(pContext, jsonResponse)
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










        fun processReturn2(vContext: Context, response: String){
            var gson = GsonBuilder().serializeNulls().create()
            var modelMeh = gson.fromJson( response , ModelMeh::class.java )
            var dealUrl = ""

            // save string to preferences
            PreferenceManager.getDefaultSharedPreferences(vContext)
                    .edit()
                    .putString(PREF_KEY_MEH_RESPONSE_STRING, response)
                    .apply()

            // set site URL
            if (modelMeh.deal != null)
            {
                // set site URL
                // set fab action button link
                if(modelMeh.deal.url != null && modelMeh.deal.url.isNotEmpty()) {
                    dealUrl = modelMeh.deal.url
                }else{
                    dealUrl = ""
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
                                vContext
                                , "Meh 2 async task"
                                , modelMeh.deal.title + "-2-Async_Task"
                                , modelMeh.deal.items[0].condition + " - " + priceLowtoHigh(modelMeh.deal)
                                , R.mipmap.ic_launcher
                                , R.mipmap.ic_launcher
                                , mehNotificationLargePhoto
                                , R.mipmap.ic_launcher
                                , dealUrl
                        )
                    }else{
                        // display notification
                        createNotification(
                                vContext
                                ,"Meh 1 fetchJSON"
                                ,modelMeh.deal.title + "-1-OkHttpClient"
                                , priceLowtoHigh(modelMeh.deal)
                                , R.mipmap.ic_launcher
                                , R.mipmap.ic_launcher
                                , mehNotificationLargePhoto
                                , R.mipmap.ic_launcher
                                , dealUrl
                        )
                    }
                }else{
                    println("set view pager to null repsonse image")
                }

            }else{
                println("set all content boxes to to null repsonses")
            }

        }



    fun createNotification( pContext: Context, tickerText: String = "", notificationTitle: String = "", notificationText: String, showactionRightButtonIcon: Int, showactionLeftButtonIcon: Int, largebitmapImageURL : String, smallIcon : Int, dealUrl: String) {
//        var handlerThread = HandlerThread("MJobExecuter_createNotification_handlerThread")
//        handlerThread.start()

//        var handler = Handler(handlerThread.looper)
//        handler.post(Runnable {
            var notificationLargeBitmap: Bitmap? = null
            try {
                notificationLargeBitmap  = Picasso
                        .with(pContext)
                        .load(largebitmapImageURL)
                        .resize(512,512)
                        .placeholder(R.drawable.ic_failed_to_load_image)
                        .error(R.drawable.ic_failed_to_load_image)
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
                        ,dealUrl
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
//        })
    }


    fun createNotification2( pContext: Context, tickerText: String = "", notificationTitle: String = "", notificationText: String, showactionRightButtonIcon: Int, showactionLeftButtonIcon: Int, largebitmapImageURL : String, smallIcon : Int, dealUrl: String) {
//            var handlerThread = HandlerThread("MJobExecuter_createNotification2_handlerThread")
//            handlerThread.start()
//
//            var handler = Handler(handlerThread.getLooper())
//            handler.post(Runnable {
                var notificationLargeBitmap: Bitmap? = null
                try {
                    notificationLargeBitmap  = Picasso
                            .with(pContext)
                            .load(largebitmapImageURL)
                            .resize(512,512)
                            .placeholder(R.drawable.ic_failed_to_load_image)
                            .error(R.drawable.ic_failed_to_load_image)
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
                            ,dealUrl
                            , NOTIFICATION_ID + 1
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
//            })
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
//                    // Extract out the title, time, and fetchJSONAsyncTask1 values
//                    val title = properties.getString("title")
//                    val time = properties.getLong("time")
//                    val fetchJSONAsyncTask1Alert = properties.getInt("fetchJSONAsyncTask1")
//
//                    // Create a new {@link ModelMeh} object
//                    return ModelMeh(title, time, fetchJSONAsyncTask1Alert)
//                }
//            } catch (e: JSONException) {
//                Log.e("extractFeatureFromJson", "Problem parsing the mehrequest JSON results", e)
//            }
//
//            return null
//        }


//    } // async task


}