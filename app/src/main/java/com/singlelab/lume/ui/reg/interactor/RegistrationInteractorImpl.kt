package com.singlelab.lume.ui.reg.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.reg.RegistrationRepository

class RegistrationInteractorImpl(private val repository: RegistrationRepository) :
    BaseInteractor(repository as BaseRepository), RegistrationInteractor {
    override suspend fun registration(profile: ProfileRequest) = repository.registration(profile)
}