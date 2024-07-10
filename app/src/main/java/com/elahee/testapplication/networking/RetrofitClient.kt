package com.btracsolutions.yesparking.networking

import com.btracsolutions.yesparking.BuildConfig
import com.btracsolutions.yesparking.preference.PrefKey
import com.btracsolutions.yesparking.preferenes.MySharedPref
import com.btracsolutions.yesparking.utils.ApiConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    private val myHttpClient: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
          //  .addInterceptor(TokenInterceptor())
            .addInterceptor(loggingInterceptor)
            .addInterceptor(NetworkConnectionInterceptor())
            .retryOnConnectionFailure(true)
//            .addInterceptor(headerAuthorizationInterceptor)
            .build()
    }


    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    // This is the fallback URL for this application

    private var baseURL = MySharedPref.getString(PrefKey.APP_BASE_URL)+ApiConstants.API_VERSION
   // private var baseURL = ""

    private var retrofit: Retrofit = getNewInstance(baseURL)

    private fun getNewInstance(url: String): Retrofit {
        var baseUrl=url
        if (!baseUrl.contains("https://")){
            if (BuildConfig.DEBUG){
                baseUrl=ApiConstants.BASE_URL+ApiConstants.DEV+ApiConstants.API_VERSION
            }else{
                baseUrl=ApiConstants.BASE_URL+ApiConstants.PROD+ApiConstants.API_VERSION
            }

        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(myHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    fun updateBaseURL(newURL: String) {
        this.baseURL = newURL
        retrofit = getNewInstance(newURL)
    }

    val getRetrofitInstance: Retrofit
        get() = retrofit

    val apiService: ApiService
        get() = getRetrofitInstance.create(ApiService::class.java)


}