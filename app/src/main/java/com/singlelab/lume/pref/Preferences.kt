package com.singlelab.lume.pref

import android.content.SharedPreferences
import com.singlelab.data.model.auth.ResponseAuth

class Preferences(private val sharedPreferences: SharedPreferences?) {

    companion object {
        const val PREF_ACCESS_TOKEN = "pref_access_token"
        const val PREF_REFRESH_TOKEN = "pref_access_token"
    }

    fun setAuth(auth: ResponseAuth) {
        setAccessToken(auth.accessToken)
        setRefreshToken(auth.refreshToken)
    }

    fun setAccessToken(accessToken: String) {
        sharedPreferences?.edit()?.putString(PREF_ACCESS_TOKEN, accessToken)?.apply()
    }

    fun getAccessToken() = sharedPreferences?.getString(PREF_ACCESS_TOKEN, "")

    fun setRefreshToken(refreshToken: String) {
        sharedPreferences?.edit()?.putString(PREF_REFRESH_TOKEN, refreshToken)?.apply()
    }

    fun getRefreshToken() = sharedPreferences?.getString(PREF_REFRESH_TOKEN, "")
}