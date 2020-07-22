package com.singlelab.net.model.auth

object AuthData {
    var accessToken: String? = null
    var refreshToken: String? = null
    var uid: String? = null
    var isAnon: Boolean = true

    fun setAuth(accessToken: String?, refreshToken: String?) {
        AuthData.accessToken = accessToken
        AuthData.refreshToken = refreshToken
    }

    fun setAuthData(accessToken: String?, refreshToken: String?, uid: String?, isAnon: Boolean) {
        AuthData.accessToken = accessToken
        AuthData.refreshToken = refreshToken
        AuthData.uid = uid
        AuthData.isAnon = isAnon
    }
}