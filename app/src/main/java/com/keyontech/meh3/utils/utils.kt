package com.example.jonesq.meh3.utils

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.example.jonesq.meh3.Models.ModelMehDeal
import com.example.jonesq.meh3.Models.ModelMehPoll
import com.keyontech.meh3.Activities.ActivityGoToSite
import com.keyontech.meh3.ActivityMain
import com.keyontech.meh3.R
import java.io.IOException
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import com.keyontech.meh3.services.MJobScheduler


//error catch when you swipe to fast it crashes
//
//then build and publish app


/**
 * Created by kot on 1/21/18.
 */

/*** Activity constants */
val ACT_EXTRA_MEH_VIDEO_LINK = "ACT_EXTRA_MEH_VIDEO_LINK"
val ACT_EXTRA_GO_TO_SITE_URL = "ACT_EXTRA_GO_TO_SITE_URL"

val FRAG_ARG_PHOTO_URI = "FRAG_ARG_PHOTO_URI"

val PREF_KEY_MEH_RESPONSE_STRING = "KEY_MEH_RESPONSE_STRING"
val PREF_KEY_SHOW_NAV_DRAWER_ONSTART = "PREF_KEY_SHOW_NAV_DRAWER_ONSTART"

val BROADDCAST_EXTRA_ACTION_SHOW_NOTIFICATION = "com.keyontech.meh3.SHOW_NOTIFICATION"

/*** this is used for the notification large image */
//    var mehNotificationLargePhoto = ""
val NOTIFICATION_ID = 333

/*** intent service */
val PREF_EXTRA_NOTIFICATION_IS_ALARM_ON = "PREF_EXTRA_NOTIFICATION_IS_ALARM_ON"
val TEST_NOTIFICATION_POLL_INTERVAL = 1000 * 15 // 15 seconds // used for test alarm

/*** job scheduler */
val JSCHEDULER_JOB_ID = 44448
val JS_PERSISTABLE_BUNDLE_DEAL_URL = "JS_PERSISTABLE_BUNDLE_DEAL_URL"
val JS_SCHEDULE_5_SECONDS: Long = 5000 // setMinimum time to run will not work 15 min is the minimum
val JS_SCHEDULE_8_HOURS: Long = 28800000 // setPeriodic 8 hours time to run will not work 15 min is the minimum
val JS_SCHEDULE_12_HOURS: Long = 43200000 // setPeriodic 8 hours time to run will not work 15 min is the minimum



fun printToToast(vContext: Context, vTag: String, vStr: String) {
    Toast.makeText(vContext, vTag + " - " + vStr, Toast.LENGTH_SHORT).show()
}
fun printToLog(vTag: String, vStr: String) {
    Log.d(vTag, vStr)
}
fun printToLog_10(vTag: String, vStr: String) {
    for (i in 1..10) {
        Log.d(vTag, vStr)
    }
}
fun printToErrorLog_10(vTag: String, vStr: String) {
    for (i in 1..10) {
        Log.e(vTag, vStr)
    }
}


// make sure the internet is turned on on the device
fun isConnected_To_Network(vContext: Context): Boolean {
    val connectivityManager = vContext.getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo

    return if (networkInfo != null && networkInfo.isConnected) {
        true
    } else {
        false
    } // if
} // isConnected_To_Network


// Check whether this job is currently scheduled.
fun isNotificationJobScheduled(context: Context): Boolean {
    val js = context.getSystemService(JobScheduler::class.java)
    val jobs = js!!.allPendingJobs ?: return false
//    for (i in jobs.indices) {
////        if (jobs[i].id == JobIds.PHOTOS_CONTENT_JOB) {
//        if (jobs[i].id == JSCHEDULER_JOB_ID) {
//            return true
//        }
//    }
//    return false

    return jobs.indices.any {
        //        if (jobs[i].id == JobIds.PHOTOS_CONTENT_JOB) {
        jobs[it].id == JSCHEDULER_JOB_ID
    }
}


fun scheduleNotificationJob(pContext: Context) {
    val componentName = ComponentName(pContext, MJobScheduler::class.java)
    /*** Job schedule paramters and conditions */
    val jobInfoBuilder = JobInfo.Builder(JSCHEDULER_JOB_ID, componentName)
    /*** this is the job to schedule  */
    var mJobScheduler = pContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

    /*** Job scheduler extras */
//            var persistableBundle: PersistableBundle
//            persistableBundle = PersistableBundle()
//            persistableBundle.putString(JS_PERSISTABLE_BUNDLE_DEAL_URL, mehDealUrl)
//            jobInfoBuilder.setExtras(persistableBundle)


/*** Job scheduler conditions  */
    /*** job should be delayed by the provided amount of time. */
//      /*** USE FOR TESTING ONLY 5 end */
//    jobInfoBuilder.setMinimumLatency(5000) // testing 5 sec.s  YOUR_TIME_INTERVAL comment out setPeriodic to use this

    /*** recur time interval, not more than once per period */
    jobInfoBuilder.setPeriodic(JS_SCHEDULE_12_HOURS)

    /*** require internet */
    jobInfoBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

    /*** persist this job across device reboots */
    jobInfoBuilder.setPersisted(true)

    /*** this is the job it self  */
    var mJobInfo = jobInfoBuilder.build()
    /*** start / schedule the job service  */
    mJobScheduler!!.schedule(mJobInfo!!)
}

fun rateApp(vContext: Context): Intent {
    val pIntent = Intent(Intent.ACTION_VIEW)
    pIntent.data = Uri.parse(vContext.getString(R.string.app_store_url))
    return pIntent
} // androidRate

fun shareApp(vContext: Context): Intent? {
    try {
        println("share app 1")
        var pSubject = ""
        var pMessage = ""
        pSubject = "Check out the Meh.com app"
        pMessage = vContext.getString(R.string.app_store_url)

        var vIntent = Intent(Intent.ACTION_SEND)
        vIntent.type = "text/plain"
        vIntent.putExtra(Intent.EXTRA_EMAIL, "")
        vIntent.putExtra(Intent.EXTRA_SUBJECT, pSubject)
        vIntent.putExtra(Intent.EXTRA_TEXT, pMessage)
        vIntent = Intent.createChooser(vIntent, "Share This App ")
        println("share app 2")


        return vIntent
    } catch (e: Exception) {
        printToLog_10(TAG, "ERROR " + e.message)
        e.printStackTrace()
        return null
    }
    // try
} // androidShareYourStatus


fun loadJsonFromFile(filename: String, context: Context): String {
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
    }

    if (vMin != vMax) {
        return "$$vMin - $$vMax"
    }else{
        return "$$vMax"
    }
}

fun getMaxVotes(poll: ModelMehPoll): Int {
    var vMax = poll.answers[0].voteCount

    for (a in poll.answers) {
        if (vMax < a.voteCount) vMax = a.voteCount
    }

    return vMax
}

fun showNotification(pContext: Context, vNotification_TickerText: String, vNotification_Title: String, vNotification_Text: String, vShow_Action_Right_Button_Icon_INT: Int, vShow_Action_Left_Button_Icon_INT: Int, vShow_Large_Icon_Bitmap: Bitmap, vShow_Small_Icon_Int: Int, vBuyURL: String, vNotificationID: Int) {
    // new --- start
    try {
        // Gets a PendingIntent containing the entire back stack
        // define what activity should appear when the user clicks the notification

        // details button activity
        var vIntentShowActivity = Intent(pContext, ActivityMain::class.java)

        // buy button activity
        var vIntentShowActivity2 = Intent(pContext, ActivityGoToSite::class.java)
            vIntentShowActivity2 .putExtra(ACT_EXTRA_GO_TO_SITE_URL, vBuyURL)

        // Android Wear
        var pIntent_AndroidWear = Intent(pContext, ActivityMain::class.java)

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        // details button
        var vPendingIntent = PendingIntent.getActivity(
                pContext,
                0,
                vIntentShowActivity,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        // buy button
        var vPendingIntent2 = PendingIntent.getActivity(
                pContext,
                0,
                vIntentShowActivity2,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        // android wear button
        var pPendingIntent_AndroidWear = PendingIntent.getActivity(
                pContext, 0, pIntent_AndroidWear, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // android wear button
        var vNotification_Action = NotificationCompat.Action.Builder(
                vShow_Small_Icon_Int, vNotification_Title, pPendingIntent_AndroidWear
        ).build()

        // Play sound
        var pNotification_Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //  BigPictureStyle --- start
        var pNotification_Big_Picture_Style = NotificationCompat.BigPictureStyle()
        pNotification_Big_Picture_Style.bigLargeIcon(vShow_Large_Icon_Bitmap)
        pNotification_Big_Picture_Style.bigPicture(vShow_Large_Icon_Bitmap)
//                    if (expandedIconUrl != null) {
//                        pNotification_Big_Picture_Style.bigLargeIcon(Picasso.with(context).load(expandedIconUrl).get());
//                    } else if (expandedIconResId > 0) {
//                        pNotification_Big_Picture_Style.bigLargeIcon(BitmapFactory.decodeResource(context.getResources(), expandedIconResId));
//                    } // if

        var pNotification_Build = NotificationCompat.Builder(pContext)
                .setTicker(vNotification_TickerText)  //  vResources.getString( R.string.polling_new_item_title ) )
                .setContentTitle(vNotification_Title) // vResources.getString( R.string.polling_new_item_title ) )
                .setContentText(vNotification_Text)
                .setLargeIcon(vShow_Large_Icon_Bitmap) // pPicasso_Image )
                .setSmallIcon(vShow_Small_Icon_Int) //   R.drawable.logo_32_x_32_2)
                .setContentIntent(vPendingIntent)

                /*** VIBRATE Lights ETC
                * ADD PERMISSION TO MANIFEST
                *      <!-- Used for START Polling Service on StartUp USE -->
                *      < uses - permission android:name="android.permission.VIBRATE" />
                *
                *      before api 26 color and vibrate are deprecated and changed
                *      https://medium.com/exploring-android/exploring-android-o-notification-channels-94cd274f604c
                */
                .setDefaults(Notification.DEFAULT_ALL)
//                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)

                // Play sound
                //            https://www.youtube.com/watch?v=WZX4ovWDzpI
                .setSound(pNotification_Sound)

                //  BigPictureStyle
                .setStyle(pNotification_Big_Picture_Style)

                // Android Wear --- start
                .extend(NotificationCompat.WearableExtender().addAction(vNotification_Action))

                .setAutoCancel(true)

                .setStyle(pNotification_Big_Picture_Style)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                .addAction(vShow_Action_Left_Button_Icon_INT, "Details", vPendingIntent)
                .addAction(vShow_Action_Right_Button_Icon_INT, "Buy", vPendingIntent2)

        var vNotification_Show = pNotification_Build.build()
        var vNotificationManager = pContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        vNotificationManager.notify(vNotificationID, vNotification_Show)
    } catch (e: Exception) {
        println("Util - showNotification - error: ")
    }

}