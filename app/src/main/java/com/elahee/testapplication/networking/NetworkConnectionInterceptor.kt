package com.btracsolutions.yesparking.networking

import com.btracsolutions.yesparking.utils.NetworkConnectivityChecker
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkConnectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkConnectivityChecker.isOnline()) {
            throw NoInternetException()
        }

        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}