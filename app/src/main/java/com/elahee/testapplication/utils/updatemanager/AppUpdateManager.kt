package com.btracsolutions.yesparking.utils.updatemanager

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class AppUpdateManager(context:Context) {

    private val TAG = "AppUpdateManager"
    val UPDATE_REQUEST_CODE = 777
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)

    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            // This example applies an flexible update. To apply a immediate update
            // instead, pass in AppUpdateType.IMMEDIATE
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                // Request the update
            } else {
                Log.d(TAG, "No Update available")
            }
        }
    }

//    private val updateLauncher = registerForActivityResult(
//        ActivityResultContracts.StartIntentSenderForResult()
//    ) { result ->
//        // handle callback
//        if (result.data == null) return@registerForActivityResult
//        if (result.resultCode == UPDATE_REQUEST_CODE) {
//            Toast.makeText(context, "Downloading stated", Toast.LENGTH_SHORT).show()
//            if (result.resultCode != Activity.RESULT_OK) {
//                Toast.makeText(context, "Downloading failed", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}