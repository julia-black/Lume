package com.singlelab.net.repositories

interface OnRefreshTokenListener {
    fun onRefreshToken(accessToken: String, refreshToken: String?)

    fun onRefreshTokenFailed()
}