package com.singlelab.lume.pref

import android.content.SharedPreferences
import com.singlelab.lume.model.auth.Auth
import com.singlelab.net.model.auth.AuthData

class Preferences(private val sharedPreferences: SharedPreferences?) {

    companion object {
        const val PREF_ACCESS_TOKEN = "pref_access_token"
        const val PREF_REFRESH_TOKEN = "pref_refresh_token"
        const val PREF_UID = "pref_uid"
        const val PREF_IS_ANON = "pref_anon"
        const val PREF_CITY_ID = "city_id"
        const val PREF_CITY_NAME = "city_name"
    }

    init {
        AuthData.setAuthData(
            getAccessToken(),
            getRefreshToken(),
            getUid(),
            isAnon(),
            getCityId(),
            getCityName()
        )
    }

    fun setAuth(auth: Auth) {
        AuthData.setAuth(auth.accessToken, auth.refreshToken)
        setAccessToken(auth.accessToken)
        setRefreshToken(auth.refreshToken)
    }

    fun clearAuth() {
        setAccessToken(null)
        setRefreshToken(null)
        setUid(null)
        setAnon(true)
        AuthData.setAuthData(null, null, null, true, null, null)
    }

    fun setUid(uid: String?) {
        AuthData.uid = uid
        sharedPreferences?.edit()?.putString(PREF_UID, uid)?.apply()
    }

    fun setAnon(isAnon: Boolean) {
        AuthData.isAnon = isAnon
        sharedPreferences?.edit()?.putBoolean(PREF_IS_ANON, isAnon)?.apply()
    }

    fun setCity(cityId: Int, cityName: String) {
        setCityId(cityId)
        setCityName(cityName)
        AuthData.cityId = cityId
        AuthData.cityName = cityName
    }

    private fun isAnon(): Boolean {
        return sharedPreferences == null || sharedPreferences.getBoolean(PREF_IS_ANON, true)
    }

    private fun setAccessToken(accessToken: String?) {
        sharedPreferences?.edit()?.putString(PREF_ACCESS_TOKEN, accessToken)?.apply()
    }

    private fun setRefreshToken(refreshToken: String?) {
        sharedPreferences?.edit()?.putString(PREF_REFRESH_TOKEN, refreshToken)?.apply()
    }

    private fun setCityId(cityId: Int) {
        sharedPreferences?.edit()?.putInt(PREF_CITY_ID, cityId)?.apply()
    }

    private fun setCityName(cityName: String) {
        sharedPreferences?.edit()?.putString(PREF_CITY_NAME, cityName)?.apply()
    }

    private fun getAccessToken() = sharedPreferences?.getString(PREF_ACCESS_TOKEN, "")

    private fun getUid() = sharedPreferences?.getString(PREF_UID, "")

    private fun getRefreshToken() = sharedPreferences?.getString(PREF_REFRESH_TOKEN, "")

    private fun getCityId() = sharedPreferences?.getInt(PREF_CITY_ID, -1)

    private fun getCityName() = sharedPreferences?.getString(PREF_CITY_NAME, "")
}