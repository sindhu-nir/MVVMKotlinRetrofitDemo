package com.btracsolutions.yesparking.preferenes

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.btracsolutions.yesparking.preference.PrefKey
import com.btracsolutions.yesparking.preferenes.datastore.Prefs

object MySharedPref{

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    fun initAppPreference(application: Application) {
        sharedPreferences =
            application.getSharedPreferences(PrefKey.APP_PREFERENCE, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun setBoolean(key: String?, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun setString(key: String?, value: String?) {
            editor.putString(key, value)
            editor.commit()

    }
    fun getString(key: String?):String? {
        var value = sharedPreferences.getString(key, "")
        return  value
    }
//    fun getCartData(): ArrayList<CartItem>? {
//        return if (sharedPreferences.getString(CART_DATA, "none").contentEquals("none")) {
//            null
//        } else {
//            Gson().fromJson<Any>(sharedPreferences.getString(CART_DATA, "none"),object : TypeToken<ArrayList<CartItem?>?>() {}.type) as ArrayList<CartItem>
//
//        }
//    }
    fun unsetString(key: String?) {
        editor.putString(key, "none")
        editor.commit()
    }

    fun getUserApiHash(): String? {
        return sharedPreferences!!.getString( PrefKey.USER_API_HASH, "0")
    }



    fun clearAll() {
        editor = sharedPreferences!!.edit()
        editor?.clear()
        editor?.commit()
    }
}