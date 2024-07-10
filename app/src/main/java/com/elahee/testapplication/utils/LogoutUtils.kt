package com.btracsolutions.yesparking.utils

import android.content.Context
import android.content.Intent
import com.btracsolutions.yesparking.activity.auth.login.LoginActivity
import com.btracsolutions.yesparking.model.CommonResponse
import com.btracsolutions.yesparking.model.UserUpdateResponse
import com.btracsolutions.yesparking.networking.ApiResponse
import com.btracsolutions.yesparking.preference.PrefKey
import com.btracsolutions.yesparking.preferenes.MySharedPref

object LogoutUtils {



    public fun removeUserLoginInfo() {
        MySharedPref.setBoolean(PrefKey.IS_LOGGEDIN, false)
        MySharedPref.setString(PrefKey.TOKEN, "")
        MySharedPref.setString(PrefKey.USER, "")
        MySharedPref.setString(PrefKey.REFRESH_TOKEN, "")
        MySharedPref.setString(PrefKey.USER_ID, "")
        MySharedPref.setString(PrefKey.PHONE, "")


    }

    fun logOut(context: Context) {
        removeUserLoginInfo()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

    }
}