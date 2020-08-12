package com.singlelab.net.repositories.auth

import com.singlelab.net.model.auth.AuthResponse
import com.singlelab.net.model.person.PersonFilledResponse
import com.singlelab.net.model.person.PersonUidResponse

interface AuthRepository {
    suspend fun sendSmsCode(phone: String): PersonUidResponse?

    suspend fun sendPushCode(phone: String, pushToken: String): PersonUidResponse?

    suspend fun auth(phone: String, code: String): AuthResponse?

    suspend fun isPersonFilled(): PersonFilledResponse?
}