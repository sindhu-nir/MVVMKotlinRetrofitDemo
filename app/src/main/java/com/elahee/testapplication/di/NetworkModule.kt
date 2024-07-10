package com.btracsolutions.yesparking.di

import android.app.Application
import com.btracsolutions.yesparking.networking.ApiService
import com.btracsolutions.yesparking.networking.RetrofitClient
import com.btracsolutions.yesparking.utils.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return RetrofitClient.getRetrofitInstance
    }

    @Provides
    fun providesTopHeadlinesApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


}