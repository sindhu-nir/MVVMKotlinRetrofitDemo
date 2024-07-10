package com.btracsolutions.yesparking.utils.alarmhelper


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.btracsolutions.yesparking.utils.AppConstants
import com.btracsolutions.yesparking.utils.Utils
import com.btracsolutions.yesparking.R
import com.btracsolutions.yesparking.utils.NotificationUtils.showEmergencyNotification
import com.btracsolutions.yesparking.utils.NotificationUtils.showSafeTripNotification

class EmergencyAlarmReceiver : BroadcastReceiver() {
    private val TAG = "EmergencyAlarmReceiver"

    override fun onReceive(context: Context, intent: Intent) {

        //if app is not visible then show push notification

        val timeInMilliSec = intent.getLongExtra(AppConstants.EXTRA_EXACT_ALARM_TIME, 0L)
        val tripid = intent.getStringExtra(AppConstants.EXTRA_EXACT_ALARM_TRIP_ID)
        val eid = intent.getStringExtra(AppConstants.EXTRA_EXACT_ALARM_E_TRIP_ID)

        Log.d(TAG, "AlarmReeived $timeInMilliSec $tripid $eid")

        when (intent.action) {

            AppConstants.ACTION_SET_EMERGENCY_REPETITIVE_EXACT -> {
                Log.d(TAG, "ACTION_SET_EMERGENCY_REPETITIVE_EXACT")
                EmergencyPushService(context).setRepetitiveAlarm(tripid)
                showEmergencyNotification(context, id = tripid)
            }

            AppConstants.EXACT_SINGLE_ALARM_ONETIME -> {
              if (tripid.contentEquals(AppConstants.TRIPID_EXTENT)) {
                when (Utils.isAppForeground()) {
                  true -> {
                    val alarmIntent = Intent(AppConstants.EMERGENCY_ALARM_RECEIVER)
                    alarmIntent.setPackage(context.packageName)
                    alarmIntent.putExtra(AppConstants.EMERGENCY_ALARM_RECEIVER, TAG)
                    context.sendBroadcast(alarmIntent)
                  }

                  else -> {
                      showSafeTripNotification(
                          context,
                          context.getString(R.string.reminder),
                          context.getString(R.string.plz_extend_your_booking_before_over),
                          id = tripid
                      )

                  }
                }
              }else {
                  showSafeTripNotification(
                      context,
                      context.getString(R.string.attention),
                      context.getString(R.string.your_booking_will_be_cancelled_soon),
                      id = tripid
                  )
              }
            }

            AppConstants.ACTION_SET_REPETITIVE_EXACT -> {

                AlarmService(context).setRepetitiveAlarm(timeInMilliSec, tripid)

                when (Utils.isAppForeground()) {
                    true -> {
//            val alarmIntent = Intent(EMERGENCY_ALARM_RECEIVER)
//            alarmIntent.setPackage(context.packageName)
//            alarmIntent.putExtra(EMERGENCY_ALARM_RECEIVER, TAG)
//            context.sendBroadcast(alarmIntent)
                        Log.d(TAG, "Inside Brodcast Reciever")

                        showSafeTripNotification(
                            context,
                            context.getString(R.string.app_name),
                            "Parking Ongoing",
                            id = tripid
                        )
                    }

                    else -> {
                        showSafeTripNotification(
                            context,
                            context.getString(R.string.app_name),
                            "Parking Ongoing",
                            id = tripid
                        )
                    }
                }
            }
        }
    }
}
