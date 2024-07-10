package com.btracsolutions.yesparking.utils

import android.util.Log
import com.btracsolutions.yesparking.BuildConfig
import com.btracsolutions.yesparking.R
import com.btracsolutions.yesparking.dailog.CommonInfoDialogFragment
import com.btracsolutions.yesparking.preference.PrefKey
import com.btracsolutions.yesparking.preferenes.MySharedPref
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings


object RemoteConfigUtils {

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private const val APP_BASE_URL_PROD = "APP_BASE_URL_PROD"
    private const val APP_BASE_URL_DEV = "APP_BASE_URL_DEV"
    private const val MAP_API_KEY_ANDROID_USER = "MAP_API_KEY_ANDROID_USER"
    private const val PAYMENT_CONFIRM_URL_DEV = "PAYMENT_CONFIRM_URL_DEV"
    private const val PAYMENT_CONFIRM_URL_PROD = "PAYMENT_CONFIRM_URL_PROD"
    private const val VERSION_CODE_ANDROID = "VERSION_CODE_ANDROID"
    private const val FORCE_UPDATE = "FORCE_UPDATE"


    fun init() {
        remoteConfig = getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                0
            } else {
                PrefKey.FIRE_BASE_REMOTE_CONFIG_INTERVAL
            }
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            // Log.d("RemoteConfigTest", "addOnCompleteListener")
            if (it.isSuccessful) {
                println("" + it.result)
            }
            storeAppCredentialsUrl()
        }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.d("RemoteConfigTest", "Updated keys: " + configUpdate.updatedKeys);

                    remoteConfig.activate().addOnCompleteListener {
                        storeAppCredentialsUrl()
                    }

            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w("RemoteConfigTest", "Config update error with code: " + error.code, error)
            }
        })
        return remoteConfig
    }


    // To store the base url from firebase remote config;
    fun storeAppCredentialsUrl() {
        // For app base url and credentials config;

        ApiConstants.MAP_API_KEY_ANDROID = remoteConfig.getString(MAP_API_KEY_ANDROID_USER)
        if (BuildConfig.DEBUG) {
            Log.d("RemoteConfigTest", "APP_BASE_URL: " + remoteConfig.getString(APP_BASE_URL_DEV))
            Log.d(
                "RemoteConfigTest",
                "PAYMENT_CONFIRM_URL: " + remoteConfig.getString(PAYMENT_CONFIRM_URL_DEV)
            )
            Log.d(
                "RemoteConfigTest",
                "MAP API KEY ANDROID: " + remoteConfig.getString(MAP_API_KEY_ANDROID_USER)
            )
            Log.d("RemoteConfigTest", "FORCE_UPDATE: " + remoteConfig.getBoolean(FORCE_UPDATE))
            Log.d("RemoteConfigTest", "VERSION_CODE_ANDROID: " + remoteConfig.getString(VERSION_CODE_ANDROID))
            MySharedPref.setString(PrefKey.APP_BASE_URL, remoteConfig.getString(APP_BASE_URL_DEV))
         //   ApiConstants.BASE_URL = remoteConfig.getString(APP_BASE_URL_DEV)
            ApiConstants.PAYMENT_CONFIRM_URL = remoteConfig.getString(PAYMENT_CONFIRM_URL_DEV)

        } else {
            Log.d("RemoteConfigTest", "APP_BASE_URL: " + remoteConfig.getString(APP_BASE_URL_PROD))
            Log.d(
                "RemoteConfigTest",
                "PAYMENT_CONFIRM_URL: " + remoteConfig.getString(PAYMENT_CONFIRM_URL_PROD)
            )
            MySharedPref.setString(PrefKey.APP_BASE_URL, remoteConfig.getString(APP_BASE_URL_PROD))
     //       ApiConstants.BASE_URL = remoteConfig.getString(APP_BASE_URL_PROD)
            ApiConstants.PAYMENT_CONFIRM_URL = remoteConfig.getString(PAYMENT_CONFIRM_URL_PROD)

        }

        ApiConstants.FORCE_UPDATE = remoteConfig.getBoolean(FORCE_UPDATE)
        ApiConstants.VERSION_CODE_ANDROID = remoteConfig.getString(VERSION_CODE_ANDROID)

    }




}