package com.btracsolutions.yesparking.utils

import android.os.CountDownTimer
import com.btracsolutions.yesparking.model.SyncData


abstract class CountUpTimer protected constructor(private val duration: Long, passedMillSec: Long) :

    CountDownTimer(duration, INTERVAL_MS) {
    abstract fun onTick(second: Int)
    abstract fun onFinish(second: Int)
    var passedMillSec = passedMillSec
    override fun onTick(msUntilFinished: Long) {
        val second = (((duration - msUntilFinished) / 1000).toInt())
        onTick(second)
    }

    override fun onFinish() {
        onTick(duration / 1000)
    }

    companion object {
        private const val INTERVAL_MS: Long = 1000
    }
}