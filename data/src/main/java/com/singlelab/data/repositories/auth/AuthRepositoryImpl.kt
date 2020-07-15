package com.singlelab.data.repositories.auth

import com.singlelab.data.model.auth.ResponsePersonUid
import com.singlelab.data.net.ApiException
import com.singlelab.data.net.AuthApi
import com.singlelab.data.repositories.BaseRepository

class AuthRepositoryImpl(private var authApi: AuthApi) : AuthRepository, BaseRepository() {
    @Throws(ApiException::class)
    override suspend fun sendCode(phone: String): ResponsePersonUid? {
        return safeApiCall(
            call = { authApi.sendCode(phone).await() },
            errorMessage = "Не удалось отправить код"
        )
    }
}