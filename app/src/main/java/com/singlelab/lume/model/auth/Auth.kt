package com.singlelab.lume.model.auth

import com.singlelab.net.model.auth.AuthResponse

class Auth(
    val accessToken: String,
    val refreshToken: String?
) {
    companion object {
        fun fromResponse(authResponse: AuthResponse?): Auth? {
            return if (authResponse != null) {
                Auth(
                    authResponse.accessToken,
                    authResponse.refreshToken
                )
            } else {
                null
            }
        }
    }
}