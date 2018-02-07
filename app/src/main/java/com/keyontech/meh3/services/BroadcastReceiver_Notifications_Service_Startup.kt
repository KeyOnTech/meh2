package com.keyontech.meh3.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager

class BroadcastReceiver_Notifications_Service_Startup : BroadcastReceiver() {
    override fun onReceive(pContext: Context, pIntent: Intent) {
        println("777   BroadcastReceiver_Notifications_Service_Startup  :  onReceive ")
        val vSharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext)
        val vAlarmIsOn = vSharedPreferences.getBoolean(IntentService_Notifications_Poll_Service.EXTRA_NOTIFICATION_IS_ALARM_ON, false)
        IntentService_Notifications_Poll_Service.setServiceAlarm(pContext, vAlarmIsOn)
    }
}