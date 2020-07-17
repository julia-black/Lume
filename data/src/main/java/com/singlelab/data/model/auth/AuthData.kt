package com.singlelab.data.model.auth

object AuthData {
    var accessToken: String? = null
    var refreshToken: String? = null
    var uid: String? = null
    var isAnon: Boolean = true

    fun setAuth(auth: Auth) {
        accessToken = auth.accessToken
        refreshToken = auth.refreshToken
    }

    fun setAuthData(accessToken: String?, refreshToken: String?, uid: String?, isAnon: Boolean) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.uid = uid
        this.isAnon = isAnon
    }
}