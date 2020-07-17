package com.singlelab.lume.ui.reg.interactor

import com.singlelab.data.model.profile.Profile
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.reg.RegistrationRepository
import com.singlelab.lume.base.BaseInteractor

class RegistrationInteractorImpl(private val repository: RegistrationRepository) :
    BaseInteractor(repository as BaseRepository), RegistrationInteractor {
    override suspend fun registration(profile: Profile) {
        return repository.registration(profile)
    }
}