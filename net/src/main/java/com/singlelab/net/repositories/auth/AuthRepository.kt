package com.singlelab.net.repositories.auth

import com.singlelab.net.model.auth.AuthResponse
import com.singlelab.net.model.person.PersonFilledResponse
import com.singlelab.net.model.person.PersonUidResponse

interface AuthRepository {
    suspend fun sendCode(phone: String): PersonUidResponse?

    suspend fun auth(phone: String, code: String): AuthResponse?

    suspend fun isPersonFilled(): PersonFilledResponse?
}