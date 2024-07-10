package com.btracsolutions.yesparking
import android.app.Application
import android.content.Context
import com.btracsolutions.yesparking.preferenes.MySharedPref.initAppPreference
import com.btracsolutions.yesparking.utils.NetworkConnectivityChecker
import com.btracsolutions.yesparking.utils.RemoteConfigUtils
import com.btracsolutions.yesparking.utils.permission.PermissionsPreferences.initPermissionSharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppPreference(this)
        initPermissionSharedPreferences(this)
        RemoteConfigUtils.init()
        NetworkConnectivityChecker.initConnectionManager(this)
    }



}