package com.singlelab.lume.ui.edit_profile.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.profile.NewProfile
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.person.PersonRepository

class EditProfileInteractorImpl(private val repository: PersonRepository) :
    EditProfileInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun updateProfile(newProfile: NewProfile) {
        repository.updateProfile(newProfile.toRequest())
    }
}