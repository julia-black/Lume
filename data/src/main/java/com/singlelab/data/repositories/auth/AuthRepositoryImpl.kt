package com.singlelab.data.repositories.auth

import com.singlelab.data.model.auth.Auth
import com.singlelab.data.model.auth.RequestPersonFilled
import com.singlelab.data.model.auth.RequestPersonUid
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class AuthRepositoryImpl(var apiUnit: ApiUnit) : AuthRepository, BaseRepository() {
    override suspend fun sendCode(phone: String): RequestPersonUid? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.authApi.sendCodeAsync(phone).await() },
            errorMessage = "Не удалось отправить код"
        )
    }

    override suspend fun auth(phone: String, code: String): Auth? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.authApi.authAsync(phone, code).await() },
            errorMessage = "Не удалось авторизоваться"
        )
    }

    override suspend fun isPersonFilled(): RequestPersonFilled? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.authApi.getIsPersonFilledAsync().await() },
            errorMessage = "Не удалось авторизоваться"
        )
    }
}