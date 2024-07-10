package com.btracsolutions.yesparking.utils.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager

class GpsStatusReceiver(private val locationCallBack: LocationCallBack? = null) :
  BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {

    val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
      locationCallBack?.gpsStatusChange()
    else locationCallBack?.gpsStatusChange()
  }

  interface LocationCallBack {
    fun gpsStatusChange()
  }
}