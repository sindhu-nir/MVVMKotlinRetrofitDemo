package com.btracsolutions.yesparking.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat.Flags
import androidx.core.content.ContextCompat.getSystemService
import com.btracsolutions.yesparking.R
import com.btracsolutions.yesparking.activity.dashboard.DashBoardActivity
import com.btracsolutions.yesparking.model.PushNotificationData
import com.btracsolutions.yesparking.preference.PrefKey
import com.btracsolutions.yesparking.preferenes.MySharedPref
import com.btracsolutions.yesparking.utils.ApiConstants
import com.btracsolutions.yesparking.utils.AppConstants
import com.btracsolutions.yesparking.utils.alarmhelper.EmergencyAlarmReceiver
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class MyFirebaseMessagingService  : FirebaseMessagingService(){
    private val TAG = "MyFcmTesting"
    private lateinit var mContext: Context

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        mContext = applicationContext
        MySharedPref.setString(PrefKey.FCM_DEVICE_TOKEN, token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
       // sendRegistrationToServer(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "remoteMessage: ${remoteMessage.data}")
        mContext = applicationContext
        //  appDatabase = AppDatabase.invoke(mContext)
        val gson = Gson()
        Log.d(
            TAG,
            "onMessageReceived Notification Message Body: ${remoteMessage.data.toString()}"
        )
        val pushNotificationData = gson.fromJson(gson.toJson(remoteMessage.data), PushNotificationData::class.java)
        Log.d(TAG, "Parsing ${pushNotificationData.body}")

        if (pushNotificationData != null) {
            var actualTitle: String? = null
            var actualDetails: String? = null
//            if (LocaleHelper.getLanguage(mContext).trim().contentEquals("bn")) {
//                pushNotificationData.title?.let {
//                    actualTitle = it
//                }
//                pushNotificationData.body?.let {
//                    actualTitle = it
//                }
//            } else {
//                actualTitle = pushNotificationData.title!!
//                actualDetails = pushNotificationData.body!!
//            }

            actualTitle = pushNotificationData.title!!
            actualDetails = pushNotificationData.body!!
            val type = pushNotificationData.type!!

            val time: Long = remoteMessage.sentTime
            val image: Uri? = remoteMessage.notification?.imageUrl
            Log.d(TAG, "message: $actualDetails title: $actualTitle time: $time uri: $image")
            val isLoggedIn: Boolean = MySharedPref.getBoolean(PrefKey.IS_LOGGEDIN)
            val isPushNotificationOn: Boolean =
                MySharedPref.getBoolean(PrefKey.IS_PUSH_NOTIFICATION_OFF)
            if (isLoggedIn && !isPushNotificationOn) {
                if (!actualTitle.isNullOrEmpty() && !actualDetails.isNullOrEmpty())
                    sentNotification(
                        actualTitle!!,
                        actualDetails!!,
                        time,
                        image,
                        type
                        )
                broadCastPush(pushNotificationData)
//                if (pushNotificationData.type.contentEquals("Auto Checkout")) {
//                    val alarmIntent = Intent(AppConstants.AUTO_CHECKOUT)
//                    alarmIntent.setPackage(mContext.packageName)
//                    alarmIntent.putExtra("title", pushNotificationData.refId)
//                    alarmIntent.putExtra(AppConstants.AUTO_CHECKOUT, pushNotificationData.refId)
//                    mContext.sendBroadcast(alarmIntent)
//                }
            }
            //  changeTaskStatusWhenPushNotificationArrive(pushNotificationData)
        }

    }

    private fun broadCastPush(pushNotificationData: PushNotificationData) {

        when(pushNotificationData.type){
            "Auto Checkout","Booking Extension","Booking Hour Extension","Checkin","Checkout","Booked","Due Cleared"->{
                val alarmIntent = Intent(AppConstants.AUTO_CHECKOUT)
                alarmIntent.setPackage(mContext.packageName)
                alarmIntent.putExtra("title", pushNotificationData.type)
                alarmIntent.putExtra("refid", pushNotificationData.refId)
                mContext.sendBroadcast(alarmIntent)
            }

            else->{
                println("")
            }
        }

    }

    private fun sentNotification(
        title: String,
        message: String,
        time: Long,
        imageUrl: Uri?,
        type:String) {
        val channelId: String = PrefKey.NOTIFICATION_CHANNEL_ID
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        PrefKey.NOTIFICATION_ID=System.currentTimeMillis().toInt()
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_yesparking_icon_round)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setWhen(time)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(message))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSoundUri)

            val intent=Intent(this, DashBoardActivity::class.java)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            notificationBuilder.setContentIntent(pendingIntent)


        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                PrefKey.NOTIFICATION_CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.lightColor = R.color.colorPrimary
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300)
            notificationManager.createNotificationChannel(channel)
        }
        if (imageUrl == null) {
            notificationManager.notify(PrefKey.NOTIFICATION_ID, notificationBuilder.build())
        } else {
            Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        Log.d("ErrorLog", "error")
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        notificationBuilder.setStyle(
                            NotificationCompat.BigPictureStyle()
                                .bigPicture(resource)
                        ).setLargeIcon(resource)
                        notificationManager.notify(
                            PrefKey.NOTIFICATION_ID,
                            notificationBuilder.build()
                        )
                    }

                })
        }
    }


}