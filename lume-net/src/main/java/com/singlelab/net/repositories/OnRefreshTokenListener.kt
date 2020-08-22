package com.singlelab.net.repositories

import com.singlelab.net.model.auth.AuthResponse

interface OnRefreshTokenListener {
    fun onRefreshToken(auth: AuthResponse?)

    fun onRefreshTokenFailed()
}