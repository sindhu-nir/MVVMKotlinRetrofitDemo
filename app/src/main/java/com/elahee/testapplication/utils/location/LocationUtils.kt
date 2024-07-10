package com.btracsolutions.yesparking.utils.location

import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat

/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::                                                                         :*/
/*::  This routine calculates the distance between two points (given the     :*/
/*::  latitude/longitude of those points). It is being used to calculate     :*/
/*::  the distance between two locations using GeoDataSource (TM) products   :*/
/*::                                                                         :*/
/*::  Definitions:                                                           :*/
/*::    Southern latitudes are negative, eastern longitudes are positive     :*/
/*::                                                                         :*/
/*::  Function parameters:                                                   :*/
/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
/*::    unit = the unit you desire for results                               :*/
/*::           where: 'M' is statute miles (default)                         :*/
/*::                  'K' is kilometers                                      :*/
/*::                  'N' is nautical miles                                  :*/
/*::  Worldwide cities and other features databases with latitude longitude  :*/
/*::  are available at https://www.geodatasource.com                         :*/
/*::                                                                         :*/
/*::  For enquiries, please contact sales@geodatasource.com                  :*/
/*::                                                                         :*/
/*::  Official Web site: https://www.geodatasource.com                       :*/
/*::                                                                         :*/
/*::           GeoDataSource.com (C) All Rights Reserved 2022                :*/
/*::                                                                         :*/
/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*
* 		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "M") + " Miles\n");
*		  System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "K") + " Kilometers\n");
*		  System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "N") + " Nautical Miles\n");
*
* */
fun calculateDistance(
  lat1: Double,
  lon1: Double,
  lat2: Double,
  lon2: Double,
  unit: String
): Double {
  return if (lat1 == lat2 && lon1 == lon2) {
    0.0
  } else {
    val theta = lon1 - lon2
    var dist =
      Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(
        Math.toRadians(lat2)
      ) * Math.cos(Math.toRadians(theta))
    dist = Math.acos(dist)
    dist = Math.toDegrees(dist)
    dist *= 60 * 1.1515
    if (unit == "K") {
      dist *= 1.609344
    } else if (unit == "N") {
      dist *= 0.8684
    }
    dist
  }
}

/**
 * Returns the `location` object as a human readable string.
 */
fun UserLocation?.toText(): String {
  return if (this != null) {
    toString(latitude, longitude)
  } else {
    "Unknown location"
  }
}

/**
 * Returns the project model `location` object from an Android location object
 */
fun android.location.Location?.toLocation(): UserLocation? {
  return if (this != null) {
    UserLocation(
      time = time,
      latitude = latitude,
      longitude = longitude
    )
  } else {
    return null
  }
}

/**
 * Returns the `location` object as a human readable string.
 */
fun android.location.Location?.toText(): String {
  return if (this != null) {
    toString(latitude, longitude)
  } else {
    "Unknown location"
  }
}

fun toString(lat: Double, lon: Double): String {
  return "($lat, $lon)"
}

/*
* Return false when location is not enabled else true
*
* */
fun Context.isLocationEnabled(): Boolean {
  val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
  return LocationManagerCompat.isLocationEnabled(locationManager)
}