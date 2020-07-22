package com.singlelab.net.repositories.auth

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.auth.AuthResponse
import com.singlelab.net.model.person.PersonFilledResponse
import com.singlelab.net.model.person.PersonUidResponse
import com.singlelab.net.repositories.BaseRepository

class AuthRepositoryImpl(var apiUnit: ApiUnit) : AuthRepository, BaseRepository() {
    override suspend fun sendCode(phone: String): PersonUidResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.authApi.sendCodeAsync(phone).await() },
            errorMessage = "Не удалось отправить код"
        )
    }

    override suspend fun auth(phone: String, code: String): AuthResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.authApi.authAsync(phone, code).await() },
            errorMessage = "Не удалось авторизоваться"
        )
    }

    override suspend fun isPersonFilled(): PersonFilledResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.authApi.getIsPersonFilledAsync().await() },
            errorMessage = "Не удалось авторизоваться"
        )
    }
}