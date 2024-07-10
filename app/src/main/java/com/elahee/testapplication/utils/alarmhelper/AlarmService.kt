package com.btracsolutions.yesparking.utils.alarmhelper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.text.format.DateFormat
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.btracsolutions.yesparking.utils.AppConstants
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AlarmService(private val context: Context) {

    private val TAG = "AndroidAlarmService"

    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    //Repeating Alarm
    fun setRepetitiveAlarm(timeInMin: Long, id: String?) {

        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.MINUTES.toMillis(timeInMin)
            // set(Calendar.MINUTE, timeInMin.toInt())
            Log.d(TAG, "Set alarm for next time - ${convertDate(this.timeInMillis)}, $timeInMin")
        }

        setAlarm(
            cal.timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = AppConstants.ACTION_SET_REPETITIVE_EXACT
                    putExtra(AppConstants.EXTRA_EXACT_ALARM_TIME, timeInMin)
                    putExtra(AppConstants.EXTRA_EXACT_ALARM_TRIP_ID, id)
                }, id!!.toInt()
            )
        )
    }

    fun setSingleAlarm(timeInMin: Long, id: String?) {

        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.MINUTES.toMillis(timeInMin)
            Log.d(TAG, "Set alarm for next time - ${convertDate(this.timeInMillis)}, $timeInMin")
        }

        setAlarm(
            cal.timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = AppConstants.EXACT_SINGLE_ALARM_ONETIME
                    putExtra(AppConstants.EXTRA_EXACT_ALARM_TIME, timeInMin)
                    putExtra(AppConstants.EXTRA_EXACT_ALARM_TRIP_ID, id)
                }, id!!.toInt()
            )
        )
    }


    fun cancel(id: String?) {

        Log.d(TAG, "cancel reqid is $id")

        alarmManager?.let {

            alarmManager.cancel(
                getPendingIntent(
                    getIntent().apply {
                        action = AppConstants.ACTION_SET_REPETITIVE_EXACT
                        putExtra(AppConstants.EXTRA_EXACT_ALARM_TIME, 1000)
                    }, id!!.toInt()
                )
            )
        }
    }

    fun cancelAll() {


        alarmManager?.let {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                alarmManager.cancelAll()
              Log.d(TAG, "cancel ALL")

            }
        }
    }

    fun cancelSingle(id: String?) {

        Log.d(TAG, "cancel trip id is $id")

        alarmManager?.let {

            alarmManager.cancel(
                getPendingIntent(
                    getIntent().apply {
                        action = AppConstants.EXACT_SINGLE_ALARM_ONETIME
                        putExtra(AppConstants.EXTRA_EXACT_ALARM_TIME, 1000)
                    }, id!!.toInt()
                )
            )
        }
    }

    private fun getPendingIntent(intent: Intent, reqCode: Int): PendingIntent {
      Log.d(TAG, "getPendingIntent $reqCode")
      return PendingIntent.getBroadcast(
            context,
            //  getRandomRequestCode(),
            reqCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

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
                          Log.d(TAG, "AlarmSet DONE canScheduleExactAlarms")

                        } else {
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                timeInMillis,
                                pendingIntent
                            )
                          Log.d(TAG, "AlarmSet DONE")

                        }
                        //  alarmManager.set(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent)
                    }

                    else -> {
                        // Ask users to go to exact alarm page in system settings.
                        context.startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                    )
                  Log.d(TAG, "AlarmSet DONE can NOT ScheduleExactAlarms")

                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                    )
                  Log.d(TAG, "AlarmSet DONE")

                }
            }

        }
    }

    private fun getIntent() : Intent{
        val intent=Intent(context, EmergencyAlarmReceiver::class.java)
        intent.setPackage(context.packageName)
        return intent
    }
    private fun getRandomRequestCode(): Int {
        val code = (0..19992).random()
        return code
    }


    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
}