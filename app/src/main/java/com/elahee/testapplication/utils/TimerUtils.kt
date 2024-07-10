package com.btracsolutions.yesparking.utils

import com.btracsolutions.yesparking.model.CheckInData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

object TimerUtils {

    fun getTimeDifferenceFromCurrentTime(checkinAt:String): Long {
        var time: Long = 0
        try {
            val utils = Utils
            val currentTime = utils.getCurrentDateTime()

            val checkInAt = utils.convertOneDateFormatToAnotherUTCtoGMT(
                checkinAt, ApiConstants.DATE_FORMAT_ISO8601,
                ApiConstants.SERVER_DATE_FORMAT_WITH_TIME
            )
            val formatter2: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date: Date = formatter2.parse(checkInAt)
            val dateCurrent: Date = formatter2.parse(currentTime)
            time = dateCurrent.getTime() - date.getTime()

            return time
        } catch (e: Exception) {
            println(e.message)
        }

        return time
    }

    fun getTimeDifferenceForFullDay(checkinAt:String): Long {
        var time: Long = 0
        try {
            val utils = Utils
            val currentTime = utils.getCurrentDateTime()
            val currentDate = utils.getCurrentDateOnly()
            val time8Am=currentDate+" 08:00:00"

            val formatter2: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date: Date = formatter2.parse(time8Am)
            val dateCurrent: Date = formatter2.parse(currentTime)
            time = dateCurrent.getTime() - date.getTime()

            return time
        } catch (e: Exception) {
            println(e.message)
        }

        return time
    }

    fun getTimeDiffBefore06PM(isForAlarm:Boolean): Long {
        var time: Long = 0
        try {
            val utils = Utils
            val currentTime = utils.getCurrentDateTime()
            val currentDate = utils.getCurrentDateOnly()
            val time6pm=currentDate+" 18:00:00"
            val formatter2: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date: Date = formatter2.parse(time6pm)
            val dateCurrent: Date = formatter2.parse(currentTime)
            if (isForAlarm){
                time = date.getTime()- dateCurrent.getTime()
            }else {
                time = dateCurrent.getTime() - date.getTime()
            }

            return time
        } catch (e: Exception) {
            println(e.message)
        }

        return time
    }

    fun getTimeDiffBefore06PMFromCheckinTime(checkinAt: String): Long {
        var time: Long = 0
        try {
            val utils = Utils

            val checkInAt = utils.convertOneDateFormatToAnotherUTCtoGMT(
                checkinAt, ApiConstants.DATE_FORMAT_ISO8601,
                ApiConstants.SERVER_DATE_FORMAT_WITH_TIME
            )
            val currentDate = utils.getCurrentDateOnly()
            val time6pm=currentDate+" 18:00:00"
            val formatter2: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date: Date = formatter2.parse(time6pm)
            val dateCurrent: Date = formatter2.parse(checkInAt)
            time = dateCurrent.getTime() - date.getTime()

            return time
        } catch (e: Exception) {
            println(e.message)
        }

        return time
    }
    fun getTimeDiffBefore07AM(): Long {
        var time: Long = 0
        try {
            val utils = Utils
            val currentTime = utils.getCurrentDateTime()
            val currentDate = utils.getCurrentDateOnly()
            val time7am=currentDate+" 07:00:00"
          //  val time7am=currentDate+" 14:00:00"
            val formatter2: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date: Date = formatter2.parse(time7am)
            val dateCurrent: Date = formatter2.parse(currentTime)
            time = dateCurrent.getTime() - date.getTime()
            return time
        } catch (e: Exception) {
            println(e.message)
        }

        return time
    }

    fun calculateParkedHours(chekinAt: String, checkOutAt: String,isFromDialog:Boolean): String {
        var parkedHour = ""
        var timeinMSec = getTimeDifference(chekinAt, checkOutAt,isFromDialog)
        if (timeinMSec.compareTo(0) == 0) {
        } else if (timeinMSec > 0) {
            val totalHour: Float = (timeinMSec.toFloat() / 3600000).toFloat()
            val hour = timeinMSec / 3600000 % 24
            val min = Math.ceil((timeinMSec.toFloat() / 60000 % 60).toDouble()).toInt()
            val sec = timeinMSec / 1000 % 60

            val hours: Long = TimeUnit.MILLISECONDS.toHours(timeinMSec)
            timeinMSec -= java.util.concurrent.TimeUnit.HOURS.toMillis(hours)
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(timeinMSec)
            timeinMSec -= java.util.concurrent.TimeUnit.MINUTES.toMillis(minutes)
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(timeinMSec)

            var hourValue = ""
            var minValue = ""
            var secValue = ""
            if (hours > 1) {
                hourValue = "" + hours + " Hours "
            } else if (hours > 0) {
                hourValue = "" + hours + " Hour "
            }
            if (minutes > 1) {
                minValue = "" + minutes + " Minutes "

            } else if (minutes > 0) {
                minValue = "" + minutes + " Minute "
            }
            if (seconds > 1) {
                secValue = "" + seconds + " Seconds"

            } else if (seconds > 0) {
                secValue = "" + seconds + " Second"
            }
            parkedHour = hourValue + minValue + secValue

        } else if (timeinMSec < 0) {

        }
        return parkedHour
    }
    private fun getTimeDifference(chekinAt: String, checkOutAt: String,isFromDialog: Boolean): Long {
        var time: Long = 0
        try {
            val utils = Utils
            val currentTime = utils.getCurrentDateTime()

            val _checkInAt = utils.convertOneDateFormatToAnotherUTCtoGMT(
                chekinAt, ApiConstants.DATE_FORMAT_ISO8601,
                ApiConstants.SERVER_DATE_FORMAT_WITH_TIME
            )

            var _checkOutAt=checkOutAt
            if (isFromDialog){
                 _checkOutAt = checkOutAt
            }else{
                 _checkOutAt = utils.convertOneDateFormatToAnotherUTCtoGMT(
                     checkOutAt, ApiConstants.DATE_FORMAT_ISO8601,
                     ApiConstants.SERVER_DATE_FORMAT_WITH_TIME
                 )!!
            }

            val formatter2: DateFormat = SimpleDateFormat(ApiConstants.SERVER_DATE_FORMAT_WITH_TIME)
            val date: Date = formatter2.parse(_checkInAt)
            val checkOutDate: Date = formatter2.parse(_checkOutAt)
            time = checkOutDate.getTime() - date.getTime()

            return time
        } catch (e: Exception) {
            println(e.message)
        }

        return time
    }

    fun calculateBookedHours(bookedAt: String): Boolean {
        var timeinMSec = getTimeDifference(bookedAt)
        if (timeinMSec > 0) {

            val hour = timeinMSec / 3600000 % 24
            val min = timeinMSec / 60000 % 60
            val sec = timeinMSec / 1000 % 60

            val hours: Long = TimeUnit.MILLISECONDS.toHours(timeinMSec)
            timeinMSec -= TimeUnit.HOURS.toMillis(hours)
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(timeinMSec)
            timeinMSec -= TimeUnit.MINUTES.toMillis(minutes)
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(timeinMSec)
            timeinMSec -= TimeUnit.SECONDS.toMillis(seconds)
            val millisecs: Long = TimeUnit.MILLISECONDS.toMillis(timeinMSec)
            //checking if Booked hour is more than one hour
            if (hours > 0) {
                return true
            } else {
                return false
            }
        }

        return false

    }

    private fun getTimeDifference(bookedAt: String): Long {
        var time: Long = 0
        try {
            val utils = Utils
            val currentTime = utils.getCurrentDateTime()

            val checkInAt = utils.convertOneDateFormatToAnotherUTCtoGMT(
                bookedAt, ApiConstants.DATE_FORMAT_ISO8601,
                ApiConstants.SERVER_DATE_FORMAT_WITH_TIME
            )
            val formatter2: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date: Date = formatter2.parse(checkInAt)
            val dateCurrent: Date = formatter2.parse(currentTime)
            time = dateCurrent.getTime() - date.getTime()

            return time
        } catch (e: Exception) {
            println(e.message)
        }

        return time
    }
}