package com.btracsolutions.yesparking.networking

import java.io.IOException

class NoInternetException : IOException() {
    override fun getLocalizedMessage(): String? {
        return "No internet connection"
    }
}