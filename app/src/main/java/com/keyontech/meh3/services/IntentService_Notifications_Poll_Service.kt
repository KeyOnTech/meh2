package com.keyontech.meh3.services

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.preference.PreferenceManager
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
// class IntentService_Notifications_Poll_Service : IntentService("IntentService_Notifications_Poll_Service") {

    private val TAG = "IntentService_Notifications_Poll_Service"


    // used for boot up to see if the alarm is set to on or off

    val EXTRA_ACTION_SHOW_NOTIFICATION = "com.keyontech.meh3.SHOW_NOTIFICATION"


    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            try {
                // fetch JSON
                println("IntentService_Notifications_Poll_Service - fetch JSON")

                // parse JSON return
                println("IntentService_Notifications_Poll_Service - parse JSON return")

                // show notification here
                println("IntentService_Notifications_Poll_Service - show notification here")

                // send broadcast
                println("onHandleIntent - send broadcast1 - send broadcast")
                sendBroadcast( Intent(EXTRA_ACTION_SHOW_NOTIFICATION))
            }  catch (e:Exception){
                println("error in IntentService_Notifications_Poll_Service " + e.message )
            }
        }
    }







    companion object {
        val EXTRA_NOTIFICATION_IS_ALARM_ON = "isAlarmOn"
        private val EXTRA_NOTIFICATION_POLL_INTERVAL = 1000 * 15 // 15 seconds

        // pg 473
        fun setServiceAlarm(pContext: Context, pAlarmIsOn: Boolean) {
            println( "444  IntentService_Notifications_Poll_Service - setServiceAlarm - pAlarmIsOn =  " + pAlarmIsOn )
            // how to...
            // https://developer.android.com/training/scheduling/alarms.html
            // Set the alarm to start at approximately 11:05 p.m.
            val pCalendar = Calendar.getInstance()
            pCalendar.timeInMillis = System.currentTimeMillis()

// works - start
            // reset hour, minutes, seconds and millis
            pCalendar.set(Calendar.HOUR_OF_DAY, 23)  // 11pm
            //        pCalendar.set( Calendar.HOUR_OF_DAY, 0 );  // 0  -  midnight
            pCalendar.set(Calendar.MINUTE, 4) // 04   -  :04
            pCalendar.set(Calendar.SECOND, 0)
            pCalendar.set(Calendar.MILLISECOND, 0)
// works - end

            val vIntent = Intent(pContext, IntentService_Notifications_Poll_Service::class.java)
            val vPendingIntent = PendingIntent.getService(pContext, 0, vIntent, 0)

//        val vAlarmManager = pContext.getSystemService( pContext.ALARM_SERVICE) as AlarmManager
            val vAlarmManager = pContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (pAlarmIsOn) {
                println( "555  IntentService_Notifications_Poll_Service - setServiceAlarm - pAlarmIsOn =  " + pAlarmIsOn )

                // USE FOR TESTING faster timer for testing notifications --- start
                // run at so many seconds over and over
                vAlarmManager.setRepeating(
                        AlarmManager.RTC
                        , System.currentTimeMillis() // start time for polling
                        , EXTRA_NOTIFICATION_POLL_INTERVAL.toLong() // run every so many seconds
                        , vPendingIntent
                )
                // USE FOR TESTING faster timer for testing notifications --- end


//                // USE FOR LIVE notifications --- START
//                vAlarmManager.setRepeating(
//                        AlarmManager.RTC
//                        , pCalendar.timeInMillis // AT time above
//                        , AlarmManager.INTERVAL_DAY // DAILY
//                        , vPendingIntent
//                )
//                // USE FOR LIVE notifications --- END

            } else {
                println( "666  IntentService_Notifications_Poll_Service - setServiceAlarm - pAlarmIsOn =  " + pAlarmIsOn )

                vAlarmManager.cancel(vPendingIntent)
                vPendingIntent.cancel()
            } // if


// save to preferences if the alarm is on or off for startup
            println("888  save to preferences   pAlarmIsOn  =  "  + pAlarmIsOn )
            PreferenceManager.getDefaultSharedPreferences(pContext)
                    .edit()
                    .putBoolean( IntentService_Notifications_Poll_Service.EXTRA_NOTIFICATION_IS_ALARM_ON , pAlarmIsOn )
                    .apply()

        } // isServiceAlarm( Context pContext )

        // pg 475
        fun isServiceAlarmOn(pContext: Context): Boolean {
            println("111  isServiceAlarmOn " )

            val vIntent = Intent(pContext, IntentService_Notifications_Poll_Service::class.java)
            val vPendingIntent = PendingIntent.getService(pContext, 0, vIntent, PendingIntent.FLAG_NO_CREATE)
            return vPendingIntent != null
        } // isServiceAlarmOn( Context pContext , boolean vIsOn )

    }





}