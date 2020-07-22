package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.profile.Profile
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.person.PersonRepository

class MyProfileInteractorImpl(private val repository: PersonRepository) : MyProfileInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun loadProfile() = Profile.fromResponse(repository.getProfile())

    override suspend fun updateImageProfile(imageStr: String) =
        repository.updateImageProfile(imageStr)?.imageUid
}