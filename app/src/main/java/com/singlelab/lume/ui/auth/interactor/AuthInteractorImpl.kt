package com.singlelab.lume.ui.auth.interactor

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.auth.Auth
import com.singlelab.data.model.auth.RequestPersonFilled
import com.singlelab.data.model.auth.RequestPersonUid
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.auth.AuthRepository
import com.singlelab.lume.base.BaseInteractor
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(private var repository: AuthRepository) :
    BaseInteractor(repository as BaseRepository), AuthInteractor {
    @Throws(ApiException::class)
    override suspend fun sendCode(phone: String): RequestPersonUid? {
        return repository.sendCode(phone)
    }

    @Throws(ApiException::class)
    override suspend fun auth(phone: String, code: String): Auth? {
        return repository.auth(phone, code)
    }

    @Throws(ApiException::class)
    override suspend fun isPersonFilled(): RequestPersonFilled? {
        return repository.isPersonFilled()
    }
}