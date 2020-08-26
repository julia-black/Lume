package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.model.profile.Badge
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.model.profile.Profile
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.person.PersonRepository

class MyProfileInteractorImpl(
    private val repository: PersonRepository,
    private val database: LumeDatabase
) : MyProfileInteractor,
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

    override suspend fun clearDatabase() {
        database.clearAllTables()
    }

    override suspend fun removePushToken() {
        repository.removePushToken()
    }

    override suspend fun loadBadges(personUid: String): List<Badge>? {
        return repository.getBadges(personUid)?.mapNotNull {
            Badge.fromResponse(it)
        }?.sortedBy { !it.isReceived }
    }
}