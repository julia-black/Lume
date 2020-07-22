package com.singlelab.data.repositories.auth

import com.singlelab.data.model.auth.Auth
import com.singlelab.data.model.auth.RequestPersonFilled
import com.singlelab.data.model.auth.RequestPersonUid

interface AuthRepository {
    suspend fun sendCode(phone: String): RequestPersonUid?

    suspend fun auth(phone: String, code: String): Auth?

    suspend fun isPersonFilled(): RequestPersonFilled?
}