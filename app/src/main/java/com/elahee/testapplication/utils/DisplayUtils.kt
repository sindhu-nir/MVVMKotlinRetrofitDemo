package com.btracsolutions.yesparking.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager


object DisplayUtils {

    data class DisplaySize(
        val height: Int,
        val width: Int,
    )

    fun Context.getDeviceDisplaySize(): DisplaySize {
        val height: Int
        val width: Int
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = wm.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            height = metrics.bounds.height() - insets.bottom - insets.top
            width = metrics.bounds.width() - insets.left - insets.right

        } else {
            val displayMetrics = DisplayMetrics()

            @Suppress("DEPRECATION")
            val display = wm.defaultDisplay // deprecated in API 30
            @Suppress("DEPRECATION")
            display.getMetrics(displayMetrics) // deprecated in API 30

            height = displayMetrics.heightPixels
            width = displayMetrics.widthPixels
        }
        return DisplaySize(height = height, width = width)
    }

    fun Context.getDeviceDisplayWidth(reductionPercent: Float = 0.16f): Int {
        val size = getDeviceDisplaySize()
        return size.width
      //  return (size.width - (reductionPercent * size.width)).toInt()
    }

    fun Context.getDeviceDisplayHeight(reductionPercent: Float = 0.16f): Int {
        val size = getDeviceDisplaySize()
        return (size.height - (reductionPercent * size.height)).toInt()
    }

    fun convertPixelsToDp(px: Int, context: Context): Float {
        val r = context.resources
        val metrics = r.displayMetrics
        return px / (metrics.densityDpi / 160f)
    }
    fun scaleToFitWidth(bitmap: Bitmap, screenWidth: Int): Bitmap? {
        val factor = screenWidth / bitmap.width.toFloat()
        return Bitmap.createScaledBitmap(
            bitmap,
            screenWidth,
            (bitmap.height * factor).toInt(),
            true
        )
    }

}