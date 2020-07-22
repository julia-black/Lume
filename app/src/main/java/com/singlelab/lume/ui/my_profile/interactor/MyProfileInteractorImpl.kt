package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.person.PersonRepository
import com.singlelab.lume.base.BaseInteractor

class MyProfileInteractorImpl(private val repository: PersonRepository) : MyProfileInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun loadProfile() = repository.getProfile()

    override suspend fun updateImageProfile(imageStr: String) =
        repository.updateImageProfile(imageStr)
}