package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.database.repository.ProfileRepository
import com.singlelab.lume.model.profile.Profile
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.person.PersonRepository

class MyProfileInteractorImpl(
    private val repository: PersonRepository,
    private val profileRepository: ProfileRepository
) : MyProfileInteractor,
    BaseInteractor(repository as BaseRepository, profileRepository) {

    override suspend fun loadProfile(): Profile? {
        val profile = Profile.fromResponse(repository.getProfile())
        profile?.let {
            val profileEntity = profileRepository.getProfile() ?: return profile
            profileEntity.updateCity(profile.cityId, profile.cityName)
            profileRepository.update(profileEntity)
        }
        return profile
    }

    override suspend fun updateImageProfile(imageStr: String) =
        repository.updateImageProfile(imageStr)?.imageUid
}