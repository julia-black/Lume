package com.singlelab.lume.ui.auth.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.auth.ResponseAuth
import com.singlelab.data.model.auth.ResponsePersonUid
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.auth.AuthRepository
import com.singlelab.lume.base.BaseInteractor
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(private var repository: AuthRepository) :
    BaseInteractor(repository as BaseRepository), AuthInteractor {
    @Throws(ApiException::class)
    override suspend fun sendCode(phone: String): ResponsePersonUid? {
        return repository.sendCode(phone)
    }

    @Throws(ApiException::class)
    override suspend fun auth(phone: String, code: String): ResponseAuth? {
        return repository.auth(phone, code)
    }
}