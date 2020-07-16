package com.singlelab.data.repositories.auth

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.auth.ResponseAuth
import com.singlelab.data.model.auth.ResponsePersonUid
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class AuthRepositoryImpl(private var apiUnit: ApiUnit) : AuthRepository, BaseRepository() {
    @Throws(ApiException::class)
    override suspend fun sendCode(phone: String): ResponsePersonUid? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.authApi.sendCodeAsync(phone).await() },
            errorMessage = "Не удалось отправить код"
        )
    }

    @Throws(ApiException::class)
    override suspend fun auth(phone: String, code: String): ResponseAuth? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.authApi.authAsync(phone, code).await() },
            errorMessage = "Не удалось авторизоваться"
        )
    }
}