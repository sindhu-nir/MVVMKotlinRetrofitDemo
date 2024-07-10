package com.btracsolutions.yesparking.networking

import com.btracsolutions.yesparking.networking.RetrofitClient.apiService
import com.btracsolutions.yesparking.preference.PrefKey
import com.btracsolutions.yesparking.preferenes.MySharedPref
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)
        val  body: ResponseBody = response.body!!
        val rawJson = body.string()
        // Check if the response indicates that the access token is expired
        if (response.code == 401) {

            // Call the refresh token API to obtain a new access token
            val newAccessToken = callRefreshTokenAPI()
            if (newAccessToken != null) {
                // Create a new request with the updated access token
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
                // Retry the request with the new access token
                return chain.proceed(newRequest)
            }
        }
        return response
    }

    private fun callRefreshTokenAPI(): String {
        val hashMap = HashMap<String, String>()
        hashMap["userId"] = MySharedPref.getString(PrefKey.USER_ID).toString()

        val refreshedToken = runBlocking {
            val response = apiService.executeTokenRefreshRequest(hashMap)
            // Update the refreshed access token and its expiration time in the session
            // sessionManager.updateAccessToken(response.accessToken, response.expiresIn)
            response.body()?.data?.accessToken
        }
        return refreshedToken.toString()
    }
}