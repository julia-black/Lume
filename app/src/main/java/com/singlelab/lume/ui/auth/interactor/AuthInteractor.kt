package com.singlelab.lume.ui.auth.interactor

import com.singlelab.data.model.auth.ResponsePersonUid
import com.singlelab.data.net.ApiException

interface AuthInteractor {
    @Throws(ApiException::class)
    suspend fun sendCode(phone: String): ResponsePersonUid?
}