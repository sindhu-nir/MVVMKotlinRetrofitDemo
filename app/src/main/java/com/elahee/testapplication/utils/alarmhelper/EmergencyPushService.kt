package com.btracsolutions.yesparking.utils.alarmhelper


import android.app.AlarmManager
import android.app.Notification.Action
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Log
import com.btracsolutions.yesparking.utils.AppConstants
import java.util.Calendar
import java.util.concurrent.TimeUnit

class EmergencyPushService(private val context: Context) {

    private val TAG = "EmergencyPushService"

    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    //Repeating Alarm
    fun setRepetitiveAlarm(id: String?) {

        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.HOURS.toMillis(1)
            Log.d(TAG, "Set alarm for next time - ${convertDate(this.timeInMillis)}")
        }

        setAlarm(
            cal.timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = AppConstants.ACTION_SET_EMERGENCY_REPETITIVE_EXACT
                    putExtra(AppConstants.EXTRA_EXACT_ALARM_TRIP_ID, id)
                }
            )
        )
    }

    fun cancel() {

        Log.d(TAG, "cancel")

        alarmManager?.let {

            alarmManager.cancel(
                getPendingIntent(
                    getIntent().apply {
                        action = AppConstants.ACTION_SET_EMERGENCY_REPETITIVE_EXACT
                        putExtra(AppConstants.EXTRA_EXACT_ALARM_TIME, 1000)
                    })
            )
        }
    }

    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            getRandomRequestCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                when {
                    // If permission is granted, proceed with scheduling exact alarms.
                    alarmManager.canScheduleExactAlarms() -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                timeInMillis,
                                pendingIntent
                            )
                        } else {
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                timeInMillis,
                                pendingIntent
                            )
                        }
                    }

                    else -> {
                        // Ask users to go to exact alarm page in system settings.
                        context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                    )
                }
            }


        }
    }

    private fun getIntent() : Intent{
        val intent=Intent(context, EmergencyAlarmReceiver::class.java)
            intent.setPackage(context.packageName)
        return intent
    }

    private fun getRandomRequestCode() = 654321

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
}