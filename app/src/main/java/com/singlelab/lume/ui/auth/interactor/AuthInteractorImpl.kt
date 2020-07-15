package com.singlelab.lume.ui.auth.interactor

import com.singlelab.data.model.auth.ResponsePersonUid
import com.singlelab.data.net.ApiException
import com.singlelab.data.repositories.auth.AuthRepository
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(private var repository: AuthRepository) :
    AuthInteractor {
    @Throws(ApiException::class)
    override suspend fun sendCode(phone: String): ResponsePersonUid? {
        return repository.sendCode(phone)
    }
}