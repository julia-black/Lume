package com.singlelab.lume.ui.auth.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.model.auth.Auth
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.auth.AuthRepository
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(
    private var repository: AuthRepository,
    private val database: LumeDatabase
) : BaseInteractor(repository as BaseRepository), AuthInteractor {

    override suspend fun sendCode(phone: String) = repository.sendSmsCode(phone)?.personUid

    override suspend fun auth(phone: String, code: String): Auth? {
        database.clearAllTables()
        return Auth.fromResponse(repository.auth(phone, code))
    }

    override suspend fun isPersonFilled() =
        repository.isPersonFilled() != null && repository.isPersonFilled()!!.isPersonFilledUp
}