package com.singlelab.lume.ui.auth.interactor

import com.singlelab.data.model.auth.Auth
import com.singlelab.data.model.auth.RequestPersonFilled
import com.singlelab.data.model.auth.RequestPersonUid

interface AuthInteractor {
    suspend fun sendCode(phone: String): RequestPersonUid?

    suspend fun auth(phone: String, code: String): Auth?

    suspend fun isPersonFilled(): RequestPersonFilled?
}