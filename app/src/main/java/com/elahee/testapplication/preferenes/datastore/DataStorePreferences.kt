package com.btracsolutions.yesparking.preferenes.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.btracsolutions.yesparking.preference.PrefKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PrefsImpl(context: Context) {


    companion object {
        private const val PREFERENCES_NAME = "DATASTORE_PREFERENCE" //data store name
        private val ACCESS_TOKEN = stringPreferencesKey(name = PrefKey.ACCESS_TOKEN)

    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = context.applicationContext.dataStore

    private val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(PREFERENCES_NAME, exception.message ?: "Error reading preferences")
                emptyPreferences()
            } else {
                throw exception
            }
        }

    fun getAccessToken(): Flow<String> = preferencesFlow
        .map { preferences -> preferences[ACCESS_TOKEN] ?: "" }

    suspend fun setAccessToken(accessToken: String) {
        dataStore.edit { preferences -> preferences[ACCESS_TOKEN] = accessToken }
    }

    private suspend fun deleteData() {
        dataStore.edit { token ->
            token.clear()
        }
    }
}




