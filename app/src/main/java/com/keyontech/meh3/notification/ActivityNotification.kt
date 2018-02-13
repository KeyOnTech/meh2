//package com.keyontech.meh3.notification
//
///**
// * Created by kot on 1/21/18.
// */
//
//
//import android.app.Notification
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.support.design.widget.Snackbar
//import android.support.v7.app.AppCompatActivity
//import com.keyontech.meh3.R
//import kotlinx.android.synthetic.main.activity_main.*
//
//
//class ActivityNotification  {
//
//    fun sendNotification( vContext: Context , vNotificationManager: NotificationManager ) {
//            val intent = Intent()
//
//            val pendingIntent = PendingIntent.getActivity(vContext,0,intent,0)
//            val notification = Notification.Builder(vContext)
//                .setTicker("ticker")
//                .setContentTitle("titlee")
//                .setContentText("textttt")
//                .setSmallIcon(R.drawable.notification_bg)
////                .setLargeIcon(R.drawable.abc_list_pressed_holo_light)
//                .setContentIntent(pendingIntent).notification
//
//            notification.flags = Notification.FLAG_AUTO_CANCEL
////            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
////            notificationManager.notify(0, notification)
//            vNotificationManager.notify( 0 , notification)
//    }
//
//}
