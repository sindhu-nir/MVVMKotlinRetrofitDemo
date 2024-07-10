package com.btracsolutions.yesparking.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.btracsolutions.yesparking.R
import com.btracsolutions.yesparking.activity.dashboard.DashBoardActivity
import com.btracsolutions.yesparking.activity.parking_timer.ParkingTimerActivity


object NotificationUtils {

    const val CHANNEL_ID = "YESPARKING"
    const val TAG = "yesparking_push"
    const val tagFirebasePush = "firebase_push"
    private const val NOTIFICATION_CHANNEL_DESCRIPTION = "YES PARKING App For You."
    private var NOTIFICATION_TRIP_ID = 1921952389
    private const val NOTIFICATION_EMERGENCY_ID = 2389

    private val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    fun showEmergencyNotification(context: Context, id: String?) {

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent: PendingIntent = ParkingTimerActivity.getEmergencyDismissIntent(
            context = context,
            tag = TAG,
            id
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_yesparking_icon_foreground)
            .setContentTitle("Emergency Ongoing")
            .setContentText("You have an active ongoing emergency.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setChannelId(CHANNEL_ID)
            .setOngoing(false)
            .setLights(Color.MAGENTA, 500, 500)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        builder.priority = PRIORITY_MAX

        manager.notify(
            NOTIFICATION_EMERGENCY_ID,
            builder.build()
        )
    }

    fun showSafeTripNotification(
        context: Context,
        textTitle: String,
        textContent: String,
        id: String?
    ) {
        Log.d(TAG, "Inside Notification Reciever")
        createNotificationChannel(context)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent: PendingIntent = DashBoardActivity.getEmergencyDismissIntent(
            context = context,
            tag = TAG,
            id
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_yesparking_icon_foreground)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(textContent))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setChannelId(CHANNEL_ID)
            .setOngoing(false)
            .setLights(Color.MAGENTA, 500, 500)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        builder.priority = PRIORITY_MAX
        NOTIFICATION_TRIP_ID = System.currentTimeMillis().toInt()

        manager.notify(
            NOTIFICATION_TRIP_ID,
            builder.build()
        )
    }

    /*
    * Firebase push Notification; Message or Trip related push, //type ->trip, tripMessage
    * */


    //areNotificationsEnabled() check from os >= 13
    fun showNotification(
        context: Context,
        textTitle: String,
        textContent: String
    ) {

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_yesparking_icon_foreground)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setChannelId(CHANNEL_ID)
            .setOngoing(false)
            .setLights(Color.MAGENTA, 500, 500)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        builder.priority = PRIORITY_MAX

        manager.notify(
            System.currentTimeMillis().toInt(),
            builder.build()
        )
    }

    fun createNotificationChannel(context: Context) {

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = NOTIFICATION_CHANNEL_DESCRIPTION
                enableLights(true)
                lockscreenVisibility = View.VISIBLE
                lightColor = Color.GREEN

            }
          manager.createNotificationChannel(channel)

        } else {
        }

    }
}