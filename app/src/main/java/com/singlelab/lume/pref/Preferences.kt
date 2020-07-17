package com.singlelab.lume.pref

import android.content.SharedPreferences
import com.singlelab.data.model.auth.Auth
import com.singlelab.data.model.auth.AuthData

class Preferences(private val sharedPreferences: SharedPreferences?) {

    companion object {
        const val PREF_ACCESS_TOKEN = "pref_access_token"
        const val PREF_REFRESH_TOKEN = "pref_refresh_token"
        const val PREF_UID = "pref_uid"
    }

    init {
        AuthData.setAuthData(getAccessToken(), getRefreshToken(), getUid())
    }

    fun setAuth(auth: Auth) {
        AuthData.setAuth(auth)
        setAccessToken(auth.accessToken)
        setRefreshToken(auth.refreshToken)
    }

    fun clearAuth() {
        setAccessToken(null)
        setRefreshToken(null)
        setUid(null)
        AuthData.setAuthData(null, null, null)
    }

    private fun setAccessToken(accessToken: String?) {
        sharedPreferences?.edit()?.putString(PREF_ACCESS_TOKEN, accessToken)?.apply()
    }

    private fun setRefreshToken(refreshToken: String?) {
        sharedPreferences?.edit()?.putString(PREF_REFRESH_TOKEN, refreshToken)?.apply()
    }

    fun setUid(uid: String?) {
        AuthData.uid = uid
        sharedPreferences?.edit()?.putString(PREF_UID, uid)?.apply()
    }

    private fun getAccessToken() = sharedPreferences?.getString(PREF_ACCESS_TOKEN, "")


    private fun getUid() = sharedPreferences?.getString(PREF_UID, "")

    private fun getRefreshToken() = sharedPreferences?.getString(PREF_REFRESH_TOKEN, "")
}