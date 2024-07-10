package com.btracsolutions.yesparking.utils

import android.R.id.input
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import com.btracsolutions.yesparking.activity.auth.login.LoginActivity
import com.btracsolutions.yesparking.model.GenericModelForDropdown
import com.btracsolutions.yesparking.preference.PrefKey
import com.btracsolutions.yesparking.preferenes.MySharedPref
import com.btracsolutions.yesparking.utils.ApiConstants.DATE_FORMAT_ISO8601
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone


 object Utils {

    fun getFormattedDate(dtStart: String): Date? {

//        val dtStart = "2010-10-15T09:27:37Z"

        lateinit var date : Date
        lateinit var dateS : String
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               // val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                val input = SimpleDateFormat("yyyy-MM-dd")
                val output = SimpleDateFormat("MMMM dd,yyyy")
                val dateP=input.parse(dtStart.substring(0,10))
                date=output.parse(dtStart.substring(0,10).toString())
                System.out.println("dateparse ok "+dtStart)

                System.out.println("dateparse ok "+date)

            }

        } catch (e: ParseException) {
            e.printStackTrace()
            System.out.println("dateparse error "+e.message)
        }

        return date

    }

    fun convertOneDateFormatToAnother(
        strTime: String?,
        dateFormat: String?,
        requiredFormat: String?
    ): String? {
        var reqTime = "--:--"
        try {
            val timeZone = TimeZone.getDefault()
            val timeZoneServer = TimeZone.getTimeZone("GMT+06:00")
            val sdfFormat = SimpleDateFormat(dateFormat, Locale.US)
            val sdfRequiredFormat = SimpleDateFormat(requiredFormat, Locale.US)
            sdfFormat.timeZone = timeZoneServer
            val formatDate = sdfFormat.parse(strTime)
            sdfRequiredFormat.timeZone = timeZone
            reqTime = sdfRequiredFormat.format(formatDate)
            System.out.print("Sys $reqTime")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return reqTime
    }
    fun convertOneDateFormatToAnotherUTCtoGMT(
        utcTimeStamp: String?,
        dateformat: String?,
        outputFormat: String?
    ): String? {
        var formattedTime: String? = null
        if (!TextUtils.isEmpty(utcTimeStamp)) {
            if (utcTimeStamp!!.contains("T")) {
                var localTime: String? = null
                val sdf = SimpleDateFormat(dateformat, Locale.US)
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
                try {
                    localTime = sdf.parse(utcTimeStamp).toString()
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val inputDateFormat: DateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US)
                inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                var date: Date? = null
                try {
                    date = inputDateFormat.parse(localTime)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val outputDateFormat: DateFormat = SimpleDateFormat(outputFormat,Locale.US)
                formattedTime = outputDateFormat.format(date)
            }
        }
        println("DATEOUTPOT "+formattedTime)
        return formattedTime!!
    }
    fun getCurrentDateTime(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }
     fun getCurrentDateTimeISO8601(): String {
         var dateformat  = SimpleDateFormat(DATE_FORMAT_ISO8601)
         dateformat.timeZone = TimeZone.getTimeZone("UTC")
         var date = Date()
     //   return SimpleDateFormat(DATE_FORMAT_ISO8601, Locale.getDefault()).format(Date())
        return dateformat.format(date)
    }
     fun getCurrentDateOnly(): String {
         return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
     }
    fun preventTwoClick(view: View) {
        view.isEnabled = false
        view.postDelayed(
            { view.isEnabled = true },
            500
        )
    }

     fun convertPixelsToDp(context: Context, pixels: Float): Float {
        val screenPixelDensity = context.resources.displayMetrics.density
        val dpValue = pixels / screenPixelDensity
        return dpValue
    }
    public fun setMediapath(path:String){
        MySharedPref.unsetString(PrefKey.MEDIAPATH)
        MySharedPref.setString(PrefKey.MEDIAPATH,path)
    }

    fun isAppForeground(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
    }

    fun generateDurationHoursList(activeFullDay:Boolean,showTill24:Boolean):ArrayList<GenericModelForDropdown>{
        var durationHourList: ArrayList<GenericModelForDropdown> = ArrayList()

        if (activeFullDay){
            durationHourList.add(GenericModelForDropdown("0", "Full day (8am to 6pm)"))
        }
        durationHourList.add(GenericModelForDropdown("1", "1 Hour"))
        durationHourList.add(GenericModelForDropdown("2", "2 Hours"))
        durationHourList.add(GenericModelForDropdown("3", "3 Hours"))
        durationHourList.add(GenericModelForDropdown("4", "4 Hours"))
        durationHourList.add(GenericModelForDropdown("5", "5 Hours"))
        durationHourList.add(GenericModelForDropdown("6", "6 Hours"))
        if (showTill24) {
            durationHourList.add(GenericModelForDropdown("7", "7 Hours"))
            durationHourList.add(GenericModelForDropdown("8", "8 Hours"))
            durationHourList.add(GenericModelForDropdown("9", "9 Hours"))
            durationHourList.add(GenericModelForDropdown("10", "10 Hours"))
            durationHourList.add(GenericModelForDropdown("11", "11 Hours"))
            durationHourList.add(GenericModelForDropdown("12", "12 Hours"))
            durationHourList.add(GenericModelForDropdown("13", "13 Hours"))
            durationHourList.add(GenericModelForDropdown("14", "14 Hours"))
            durationHourList.add(GenericModelForDropdown("15", "15 Hours"))
            durationHourList.add(GenericModelForDropdown("16", "16 Hours"))
            durationHourList.add(GenericModelForDropdown("17", "17 Hours"))
            durationHourList.add(GenericModelForDropdown("18", "18 Hours"))
            durationHourList.add(GenericModelForDropdown("19", "19 Hours"))
            durationHourList.add(GenericModelForDropdown("20", "20 Hours"))
            durationHourList.add(GenericModelForDropdown("21", "21 Hours"))
            durationHourList.add(GenericModelForDropdown("22", "22 Hours"))
            durationHourList.add(GenericModelForDropdown("23", "23 Hours"))
            durationHourList.add(GenericModelForDropdown("24", "24 Hours"))
        }

        return durationHourList
    }


}