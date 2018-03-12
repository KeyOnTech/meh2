package com.keyontech.meh3.services

import android.content.AsyncTaskLoader
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import com.keyontech.meh3.Models.JSONUrL
import com.keyontech.meh3.Models.ModelMeh
import com.keyontech.meh3.Models.modelJobExecuterAsyncTaskParams
import com.keyontech.meh3.R
import com.keyontech.meh3.utils.*
import com.squareup.picasso.Picasso
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset


//class MehAsyncTaskLoader(cContext: Context) : AsyncTaskLoader<String>(cContext) { // works
class MehAsyncTaskLoader(cContext: Context, val mTaskParamsBundle: Bundle?) : AsyncTaskLoader<String>(cContext) { // works
    /*** this is used for the notification large image */
    private var mehNotificationLargePhoto = ""
//    var jsonRespnse = ""

    override fun onStartLoading() {
        println("MehAsyncTaskLoader  - onStartLoading")

//        super.onStartLoading()
        //onStartLoading() can  be likened to OnPreExecute in AsyncTask

        println("MehAsyncTaskLoader  - onStartLoading - mTaskParamsBundle?.get(ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_JSON) " + mTaskParamsBundle?.get(ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_JSON) )

//        val jsonRespnse = ""
//        jsonRespnse = mTaskParamsBundle?.getString(ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_JSON)

//        if ( jsonRespnse.isNullOrEmpty() ) {
////        if (mTaskParamsBundle?.get(ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_LOADER)) {
            forceLoad()
//        } else {
//            deliverResult(jsonRespnse)
//        }

        //forceLoad()
        //without calling forceLoad() loadInBackground will not execute
    }


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    override fun deliverResult(newJSONResponse: String?) {
        println("MehAsyncTaskLoader  - deliverResult - $newJSONResponse")
        super.deliverResult(newJSONResponse)

//        ASYNCTASKLOADER_BUNDLE_KEY_RELOAD_JSON
//        super.deliverResult(newJSONResponse)

/*
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (apps != null) {
                onReleaseResources(apps)
            }

        }
        val oldApps = mApps
        mApps = apps

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(apps)
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldApps != null) {
            onReleaseResources(oldApps)
        }
*/
    }

    // background work
    override fun loadInBackground(): String {
//    override fun doInBackground(vararg params: Object?): String? {
        /*
         The results of loadInBackground() are automatically delivered to the UI thread
         , by way of the onLoadFinished() LoaderManager callback. forceLoad() must be called before loadInBackground() executes
         */
        println("MehAsyncTaskLoader  -   doInBackground  ----   ")
//        println("MehAsyncTaskLoader  -   doInBackground  ----   mTaskParamsBundle = " + mTaskParamsBundle)
//        println("MehAsyncTaskLoader  -   doInBackground  ----   mTaskParams[0] = " + mTaskParamsBundle?.get(AsTL_BUNDLE_KEY_DEAL_URL))
//        println("MehAsyncTaskLoader  -   doInBackground  ----   mQueryString = " + mQueryString)

//        var vURL = params[0]!!.dealUrl

//        val uString = fetchJSON_AsyncTask(vContext, vURL)
//        val uString = fetchJSON_AsyncTask(context, mTaskParamsBundle?.get(AsTL_BUNDLE_KEY_DEAL_URL))
        return fetchJSON_AsyncTask(context)
//        val uString = fetchJSON_AsyncTask(context)
//        return uString + " - MehAsyncTaskLoader - Background long running task finishes..."
    }


//    override fun doInBackground(vararg params: modelJobExecuterAsyncTaskParams?): String? {
//        var vContext = params[0]!!.cContext
//        var vURL = params[0]!!.dealUrl
//
//        val uString = fetchJSON_AsyncTask(vContext, vURL)
//        return uString + "MJobExecuter - Background long running task finishes..."
//    }




        @Throws(IOException::class)
//        fun fetchJSON_AsyncTask(pContext: Context, pDealUrl: String = ""): String {
        fun fetchJSON_AsyncTask(pContext: Context): String {
            var gson = GsonBuilder().serializeNulls().create() // include null opjects when null
            var urlFile = loadJsonFromFile("url.json", pContext)
            var jsonOutput = gson.fromJson( urlFile , JSONUrL::class.java )
            var jsonURL = jsonOutput.mehurl

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
//                println("666ccc    jsonResponse = " + jsonResponse )
//                processReturn2(pContext, jsonResponse)
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


    fun processReturn2(pContext: Context, jsonResponse: String){
        var gson = GsonBuilder().serializeNulls().create()
        var modelMeh = gson.fromJson(jsonResponse, ModelMeh::class.java)
        var dealUrl = ""
        var notificationText = ""

        /*** set site URL */
        if (modelMeh.deal != null)
        {
            // set site URL
            // set fab action button link
            if(modelMeh.deal.url != null && modelMeh.deal.url.isNotEmpty()) {
                dealUrl = modelMeh.deal.url
            }else{
                dealUrl = ""
//                println("set Deal Url here")
            }

            // set notification large image
            if (modelMeh.deal.photos != null) {
                if(modelMeh.deal.photos[0].isNotEmpty()) {
                    mehNotificationLargePhoto = modelMeh.deal.photos[0]
                }else {
                    mehNotificationLargePhoto= ""
//                    println("set default large photo image here")
                }

                if ((modelMeh.deal.items != null) && (modelMeh.deal.items[0].condition != "" && modelMeh.deal.items[0].condition != null)) {
                    notificationText = modelMeh.deal.items[0].condition + " - " + priceLowtoHigh(modelMeh.deal)
                }else{
                    notificationText = priceLowtoHigh(modelMeh.deal)
                }

                if (isNewDeal(pContext, notificationText)) {
                    /*** show notification */
                    createNotification(
                            pContext
                            , modelMeh.deal.title
                            , modelMeh.deal.title
                            , notificationText
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

    fun createNotification(pContext: Context, tickerText: String = "", notificationTitle: String = "", notificationText: String, showactionRightButtonIcon: Int, showactionLeftButtonIcon: Int, largebitmapImageURL : String, smallIcon : Int, dealUrl: String) {
        var notificationLargeBitmap: Bitmap? = null
        try {
            notificationLargeBitmap  = Picasso.with(pContext)
                    .load(largebitmapImageURL)
                    .resize(512,512)
                    .placeholder(R.drawable.ic_failed_to_load_image)
                    .error(R.drawable.ic_failed_to_load_image)
                    .get()

            showNotification(
                    pContext
                    , tickerText
                    , notificationTitle
                    , notificationText
                    , showactionRightButtonIcon
                    , showactionLeftButtonIcon
                    , notificationLargeBitmap
                    , smallIcon
                    , dealUrl
                    , NOTIFICATION_ID
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