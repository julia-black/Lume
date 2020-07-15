package com.singlelab.data.repositories.auth

import com.singlelab.data.model.auth.ResponseAuth
import com.singlelab.data.model.auth.ResponsePersonUid
import com.singlelab.data.net.ApiException

interface AuthRepository {

    @Throws(ApiException::class)
    suspend fun sendCode(phone: String): ResponsePersonUid?

    @Throws(ApiException::class)
    suspend fun auth(phone: String, code: String): ResponseAuth?
}