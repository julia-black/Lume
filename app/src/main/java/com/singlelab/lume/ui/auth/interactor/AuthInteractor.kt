package com.singlelab.lume.ui.auth.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.auth.ResponseAuth
import com.singlelab.data.model.auth.ResponsePersonUid

interface AuthInteractor {
    @Throws(ApiException::class)
    suspend fun sendCode(phone: String): ResponsePersonUid?

    @Throws(ApiException::class)
    suspend fun auth(phone: String, code: String): ResponseAuth?
}