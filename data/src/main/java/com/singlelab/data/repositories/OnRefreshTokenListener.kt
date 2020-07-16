package com.singlelab.data.repositories

import com.singlelab.data.model.auth.ResponseAuth

interface OnRefreshTokenListener {
    fun onRefreshToken(responseAuth: ResponseAuth?)
}