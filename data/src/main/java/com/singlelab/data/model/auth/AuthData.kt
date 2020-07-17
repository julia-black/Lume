package com.singlelab.data.model.auth

object AuthData {
    var accessToken: String? = null
    var refreshToken: String? = null
    var uid: String? = null

    fun setAuth(auth: Auth) {
        accessToken = auth.accessToken
        refreshToken = auth.refreshToken
    }

    fun setAuthData(accessToken: String?, refreshToken: String?, uid: String?) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.uid = uid
    }
}