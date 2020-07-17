package com.singlelab.data.repositories

import com.singlelab.data.model.auth.Auth

interface OnRefreshTokenListener {
    fun onRefreshToken(auth: Auth?)
}