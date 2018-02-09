package com.example.jonesq.meh3.utils

import android.app.Notification
import android.app.NotificationManager
import android.media.RingtoneManager
//import jdk.nashorn.internal.objects.NativeJava.extend
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.example.jonesq.meh3.Models.ModelMehDeal
import com.keyontech.meh3.ActivityMain
import java.io.IOException


/**
 * Created by kot on 1/21/18.
 */

    /*** Activity constants */
    val KEY_PHOTO_URI = "KEY_PHOTO_URI"
    val KEY_MEH_RESPONSE_STRING = "KEY_MEH_RESPONSE_STRING"
    val KEY_MEH_VIDEO_LINK = "KEY_MEH_VIDEO_LINK"
    val EXTRA_GO_TO_SITE_URL = "EXTRA_GO_TO_SITE_URL"
    val EXTRA_ACTION_SHOW_NOTIFICATION = "com.keyontech.meh3.SHOW_NOTIFICATION"


    val JSONHeaderContentType = "Content-Type: application/json"
    val JSONHeaderAccept= "Accept: application/json"
    //Content-Type: application/json
    //Accept: application/json


    /*** this is used for the notification large image */
    var mehNotificationLargePhoto = ""
    val NOTIFICATION_ID = 333


    /*** used by intent service */
    val EXTRA_NOTIFICATION_IS_ALARM_ON = "isAlarmOn"
    val EXTRA_NOTIFICATION_POLL_INTERVAL = 1000 * 15 // 15 seconds
//    var EXTRA_JSON_RESPONSE = ""



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
    //            print(" \r\n 44401: price " + i.price.toInt() )
        }

        if (vMin != vMax) {
            return "$$vMin - $$vMax"
        }else{
            return "$$vMax"
        }
    }



    fun showNotification(pContext: Context, vNotification_TickerText: String, vNotification_Title: String, vNotification_Text: String, vShow_Action_Right_Button_Icon_INT: Int, vShow_Action_Left_Button_Icon_INT: Int, vShow_Large_Icon_Bitmap: Bitmap, vShow_Small_Icon_Int: Int) {
        // new --- start
        try {
            // Gets a PendingIntent containing the entire back stack
            // define what activity should appear when the user clicks the notification
            //        Intent vIntentShowActivity = new Intent( pContext , Controller_Activity_MainActivity.class);
            var vIntentShowActivity = Intent()
            // buy --- start
    //            var vIntentShowActivity2 = Meh_API_v1.buyThisItem(pContext)
    //            var vIntentShowActivity2 = Intent(pContext, this@MainActivity)
            var vIntentShowActivity2 = Intent()
            // buy --- end
            // Android Wear --- start
    //            var pIntent_AndroidWear = Intent(pContext, this@MainActivity)
            var pIntent_AndroidWear = Intent()
            // android wear --- end


            // Because clicking the notification opens a new ("special") activity, there's
            // no need to create an artificial back stack.
            var vPendingIntent = PendingIntent.getActivity(
                    pContext,
                    0,
                    vIntentShowActivity,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            // buy --- start
            var vPendingIntent2 = PendingIntent.getActivity(
                    pContext,
                    0,
                    vIntentShowActivity2,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            // buy --- end

            // android wear --- start
            var pPendingIntent_AndroidWear = PendingIntent.getActivity(
                    pContext, 0, pIntent_AndroidWear, PendingIntent.FLAG_UPDATE_CURRENT
            )
            // android wear --- end

            // android wear -- start
            var vNotification_Action = NotificationCompat.Action.Builder(
                    vShow_Small_Icon_Int, vNotification_Title, pPendingIntent_AndroidWear
            ).build()
            // android wear --- end

            // Play sound --- start
            var pNotification_Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            // Play sound --- end


            //  BigPictureStyle --- start
            var pNotification_Big_Picture_Style = NotificationCompat.BigPictureStyle()
            //                    if (expandedIconUrl != null) {
            //                        pNotification_Big_Picture_Style.bigLargeIcon(Picasso.with(context).load(expandedIconUrl).get());
            //                    } else if (expandedIconResId > 0) {
            //                        pNotification_Big_Picture_Style.bigLargeIcon(BitmapFactory.decodeResource(context.getResources(), expandedIconResId));
            //                    } // if
            pNotification_Big_Picture_Style.bigLargeIcon(vShow_Large_Icon_Bitmap)
            pNotification_Big_Picture_Style.bigPicture(vShow_Large_Icon_Bitmap)
            //  BigPictureStyle --- end


            var pNotification_Build = NotificationCompat.Builder(pContext)
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

            var vNotification_Show = pNotification_Build.build()

            var vNotificationManager = pContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            vNotificationManager.notify(NOTIFICATION_ID, vNotification_Show)

        } catch (e: Exception) {
            println("showNotification - error")
        }

    } // showNotification








































/*
    //    public static void showNotification( Context vContext , int vCount , String vNotification_TickerTitle , String vNotification_Title , String vNotification_Message , int vShow_Small_Icon_INT , int vShow_Small_Action_Icon_INT   )
    fun showNotification(vContext: Context, vNotification_TickerText: String, vNotification_Title: String, vNotification_Text: String, vShow_Action_Right_Button_Icon_INT: Int, vShow_Action_Left_Button_Icon_INT: Int, vShow_Large_Icon_Bitmap: Bitmap, vShow_Small_Icon_Int: Int) {
        // new --- start
        try {
            // show notification

            // Gets a PendingIntent containing the entire back stack
            // define what activity should appear when the user clicks the notification
            //        Intent vIntentShowActivity = new Intent( vContext , Controller_Activity_MainActivity.class);
            val vIntentShowActivity = Intent(vContext, ActivityMain::class.java)
            // buy --- start
//            val vIntentShowActivity2 = Meh_API_v1.buyThisItem(vContext)
            val vIntentShowActivity2 = ActivityMain(vContext)
            // buy --- end
            // Android Wear --- start
            val pIntent_AndroidWear = Intent(vContext, ActivityMain::class.java)
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

            pNotification_Big_Picture_Style.bigLargeIcon(vShow_Large_Icon_Bitmap)
            pNotification_Big_Picture_Style.bigPicture(vShow_Large_Icon_Bitmap)
            //  BigPictureStyle --- end


            /// notification text placed here temporarily...
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
                    //                    // BigPictureStyle --- end

                    //                            .setStyle(new Notification.BigTextStyle().bigText(pContentText))

                    // http://stackoverflow.com/questions/7238243/how-to-use-notification-with-sound-and-vibration
                    .setDefaults(Notification.DEFAULT_ALL)
                    //                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))


                    .addAction(
                            vShow_Action_Left_Button_Icon_INT, "Details", vPendingIntent)

                    .addAction(
                            vShow_Action_Right_Button_Icon_INT, "Buy", vPendingIntent2) // buy

            val vNotification_Show = pNotification_Build.build()


            val vNotificationManager = vContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            vNotificationManager.notify(NOTIFICATION_ID, vNotification_Show)
        } catch (e: Exception) {
            printToErrorLog_10(TAG, "showNotification")
        }
        // try

        /// new --- end

    } // showNotification
*/




