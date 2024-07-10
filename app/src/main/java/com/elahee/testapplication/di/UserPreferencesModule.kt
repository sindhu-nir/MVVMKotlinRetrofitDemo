package com.btracsolutions.yesparking.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.btracsolutions.yesparking.networking.ApiService
import com.btracsolutions.yesparking.networking.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    // just my preference of naming including the package name
    name = "com.btracsolutions.yesparking.data_store_preferences"
)

@Module
@InstallIn(SingletonComponent::class)
object UserPreferencesModule {
    @Singleton
    @Provides
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences>{
        return applicationContext.userDataStore
    }
}