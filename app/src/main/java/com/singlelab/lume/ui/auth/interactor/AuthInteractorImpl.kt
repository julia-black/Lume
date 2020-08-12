package com.singlelab.lume.ui.auth.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.auth.Auth
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.auth.AuthRepository
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(private var repository: AuthRepository) :
    BaseInteractor(repository as BaseRepository), AuthInteractor {
    override suspend fun sendCode(phone: String, pushToken: String?): String? {
        return if (pushToken != null) {
            repository.sendPushCode(phone, pushToken)?.personUid
        } else {
            repository.sendSmsCode(phone)?.personUid
        }
    }

    override suspend fun auth(phone: String, code: String): Auth? {
        return Auth.fromResponse(repository.auth(phone, code))
    }

    override suspend fun isPersonFilled(): Boolean {
        return repository.isPersonFilled() != null && repository.isPersonFilled()!!.isPersonFilledUp
    }
}