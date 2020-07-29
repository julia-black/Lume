package com.singlelab.lume.ui.auth.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.database.entity.ProfileEntity
import com.singlelab.lume.database.repository.ProfileRepository
import com.singlelab.lume.model.auth.Auth
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.auth.AuthRepository
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(
    private var repository: AuthRepository,
    private val profileRepository: ProfileRepository
) :
    BaseInteractor(repository as BaseRepository, profileRepository), AuthInteractor {
    override suspend fun sendCode(phone: String): String? {
        return repository.sendCode(phone)?.personUid
    }

    override suspend fun auth(personUid: String, phone: String, code: String): Auth? {
        val auth = Auth.fromResponse(repository.auth(phone, code))
        profileRepository.clear()
        auth?.let {
            val profile = ProfileEntity(personUid, it.accessToken, it.refreshToken)
            profileRepository.insert(profile)
        }
        return auth
    }

    override suspend fun isPersonFilled(): Boolean {
        return repository.isPersonFilled() != null && repository.isPersonFilled()!!.isPersonFilledUp
    }
}