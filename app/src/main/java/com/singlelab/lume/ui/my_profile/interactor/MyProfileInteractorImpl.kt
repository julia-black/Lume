package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.model.profile.Profile
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.person.PersonRepository

class MyProfileInteractorImpl(private val repository: PersonRepository) : MyProfileInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun loadProfile() = Profile.fromResponse(repository.getProfile())

    override suspend fun loadFriends(personUid: String): List<Person>? {
        return repository.getFriends(personUid)?.mapNotNull {
            Person.fromResponse(it)
        }
    }

    override suspend fun updateImageProfile(imageStr: String): String? {
        val profileResponse = repository.updateProfile(ProfileRequest(image = imageStr))
        return profileResponse?.imageContentUid
    }

    override suspend fun updatePushToken(token: String?) {
        repository.updateProfile(ProfileRequest(token = token))
    }
}