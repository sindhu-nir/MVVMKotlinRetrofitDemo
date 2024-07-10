package com.btracsolutions.yesparking.preferenes.datastore

import kotlinx.coroutines.flow.Flow

interface Prefs {



  fun getAccessToken(): Flow<String>
  suspend fun setAccessToken(accessToken: String)

}