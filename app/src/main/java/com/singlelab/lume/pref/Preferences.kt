package com.singlelab.lume.pref

import android.content.SharedPreferences
import com.singlelab.data.model.auth.Headers
import com.singlelab.data.model.auth.ResponseAuth

class Preferences(private val sharedPreferences: SharedPreferences?) {

    companion object {
        const val PREF_ACCESS_TOKEN = "pref_access_token"
        const val PREF_REFRESH_TOKEN = "pref_refresh_token"
        const val PREF_UID = "pref_uid"
    }

    fun setAuth(auth: ResponseAuth) {
        setAccessToken(auth.accessToken)
        setRefreshToken(auth.refreshToken)
    }

    fun clearAuth() {
        setAccessToken(null)
        setRefreshToken(null)
        setUid(null)
    }

    fun getHeaders(): Headers {
        return Headers(
            accessToken = getAccessToken(),
            refreshToken = getRefreshToken(),
            uid = getUid()
        )
    }

    fun setAccessToken(accessToken: String?) {
        sharedPreferences?.edit()?.putString(PREF_ACCESS_TOKEN, accessToken)?.apply()
    }

    fun getAccessToken() = sharedPreferences?.getString(PREF_ACCESS_TOKEN, "")

    fun setRefreshToken(refreshToken: String?) {
        sharedPreferences?.edit()?.putString(PREF_REFRESH_TOKEN, refreshToken)?.apply()
    }

    fun setUid(uid: String?) {
        sharedPreferences?.edit()?.putString(PREF_UID, uid)?.apply()
    }

    fun getUid() = sharedPreferences?.getString(PREF_UID, "")

    fun getRefreshToken() = sharedPreferences?.getString(PREF_REFRESH_TOKEN, "")


}