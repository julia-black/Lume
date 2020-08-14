package com.singlelab.net.repositories.auth

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.auth.AuthResponse
import com.singlelab.net.model.person.PersonFilledResponse
import com.singlelab.net.model.person.PersonUidResponse
import com.singlelab.net.repositories.BaseRepository

class AuthRepositoryImpl(var apiUnit: ApiUnit) : AuthRepository, BaseRepository(apiUnit) {
    override suspend fun sendSmsCode(phone: String): PersonUidResponse? {
        return safeApiCall(
            call = { apiUnit.authApi.sendSmsCodeAsync(phone).await() },
            errorMessage = "Не удалось отправить код"
        )
    }

    override suspend fun sendPushCode(phone: String, pushToken: String): PersonUidResponse? {
        return safeApiCall(
            call = { apiUnit.authApi.sendPushCodeAsync(phone, pushToken).await() },
            errorMessage = "Не удалось отправить код"
        )
    }

    override suspend fun auth(phone: String, code: String): AuthResponse? {
        return safeApiCall(
            call = { apiUnit.authApi.authAsync(phone, code).await() },
            errorMessage = "Не удалось авторизоваться"
        )
    }

    override suspend fun isPersonFilled(): PersonFilledResponse? {
        return safeApiCall(
            call = { apiUnit.authApi.getIsPersonFilledAsync().await() },
            errorMessage = "Не удалось авторизоваться"
        )
    }
}