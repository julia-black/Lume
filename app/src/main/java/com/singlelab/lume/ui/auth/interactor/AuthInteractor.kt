package com.singlelab.lume.ui.auth.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.auth.Auth
import com.singlelab.data.model.auth.RequestPersonFilled
import com.singlelab.data.model.auth.RequestPersonUid

interface AuthInteractor {
    @Throws(ApiException::class)
    suspend fun sendCode(phone: String): RequestPersonUid?

    @Throws(ApiException::class)
    suspend fun auth(phone: String, code: String): Auth?

    @Throws(ApiException::class)
    suspend fun isPersonFilled(): RequestPersonFilled?
}