package com.btracsolutions.yesparking.utils.permission

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

sealed class Permissions(vararg val permissions: String) {
    // Individual permissions
    object Camera : Permissions(CAMERA)
    // Bundled permissions
    object ImagePick : Permissions(*getImagePickPermissions())
    object ImgVidCamPerm : Permissions(*getImgVidCamPermission())
    object ImgVidPerm : Permissions(*getImgVidPermission())
    object AudioPickPerm : Permissions(*getAudioPermission())
    // Grouped permissions
    object Location : Permissions(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    companion object {
        private fun getImagePickPermissions(): Array<String> {
            return if (PermissionManager.sdkEqOrAbove33()) {
                arrayOf(READ_MEDIA_IMAGES)
            } else if (PermissionManager.sdkEqOrAbove29()) {
                arrayOf(READ_EXTERNAL_STORAGE)
            } else {
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                )
            }
        }
        private fun getImgVidCamPermission(): Array<String> {
            return if (PermissionManager.sdkEqOrAbove33()) {
                arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, CAMERA)
            } else if (PermissionManager.sdkEqOrAbove29()) {
                arrayOf(READ_EXTERNAL_STORAGE, CAMERA)
            } else {
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE,
                    CAMERA
                )
            }
        }
        private fun getAudioPermission(): Array<String> {
            return if (PermissionManager.sdkEqOrAbove33()) {
                arrayOf(READ_MEDIA_AUDIO)
            } else if (PermissionManager.sdkEqOrAbove29()) {
                arrayOf(READ_EXTERNAL_STORAGE)
            } else {
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                )
            }
        }
        private fun getImgVidPermission(): Array<String> {
            return if (PermissionManager.sdkEqOrAbove33()) {
                arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO)
            } else if (PermissionManager.sdkEqOrAbove29()) {
                arrayOf(READ_EXTERNAL_STORAGE)
            } else {
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }
}