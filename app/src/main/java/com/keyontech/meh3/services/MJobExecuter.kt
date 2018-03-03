package com.keyontech.meh3.services

/**
 * Created by kot on 2/26/18.
 */

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.util.Log
import com.keyontech.meh3.Models.JSONUrL
import com.keyontech.meh3.Models.ModelMeh
import com.keyontech.meh3.utils.*
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

open class MJobExecuter : AsyncTask<modelJobExecuterAsyncTaskParams, Void, String>() {
    /*** this is used for the notification large image */
    var mehNotificationLargePhoto = ""

    override fun doInBackground(vararg params: modelJobExecuterAsyncTaskParams?): String? {
        var vContext = params[0]!!.cContext
        var vURL = params[0]!!.dealUrl

        val uString = fetchJSON_AsyncTask(vContext, vURL)
        return uString + "MJobExecuter - Background long running task finishes..."
    }


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
//
            }
        }
    }


    fun createNotification2( pContext: Context, tickerText: String = "", notificationTitle: String = "", notificationText: String, showactionRightButtonIcon: Int, showactionLeftButtonIcon: Int, largebitmapImageURL : String, smallIcon : Int, dealUrl: String) {
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
//
            }
        }
    }

}