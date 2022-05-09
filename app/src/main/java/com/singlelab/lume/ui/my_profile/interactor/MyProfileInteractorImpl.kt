package com.singlelab.lume.ui.my_profile.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.database.LumeDatabase
import com.singlelab.lume.database.repository.ProfileRepository
import com.singlelab.lume.model.profile.Badge
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.model.profile.Profile
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.person.PersonRepository

class MyProfileInteractorImpl(
    private val repository: PersonRepository,
    private val localRepository: ProfileRepository,
    private val database: LumeDatabase
) : MyProfileInteractor,
    BaseInteractor(repository as BaseRepository) {

    override suspend fun loadProfile() = Profile.fromResponse(repository.getProfile())

    override suspend fun loadFriends(personUid: String) =
        repository.getFriends(personUid)?.mapNotNull {
            Person.fromResponse(it)
        }

    override suspend fun updateImageProfile(imageStr: String, miniImageStr: String): String? =
        repository.updateProfile(ProfileRequest(image = imageStr, miniImage = miniImageStr))
            ?.imageContentUid

    override suspend fun updatePushToken(token: String?) {
        repository.updateProfile(ProfileRequest(token = token))
    }

    override suspend fun clearDatabase() {
        database.clearAllTables()
    }

    override suspend fun removePushToken() {
        repository.removePushToken()
    }

    override suspend fun loadBadges(personUid: String) =
        repository.getBadges(personUid)?.mapNotNull {
            Badge.fromResponse(it)
        }?.sortedBy { !it.isReceived }

    override suspend fun loadProfileFromCache(uid: String) =
        Profile.fromEntity(localRepository.get(uid))

    override suspend fun saveProfile(profile: Profile) {
        localRepository.insert(profile.toEntity())
    }

    override suspend fun loadFriendsFromCache() =
        localRepository.getFriends().mapNotNull { Person.fromEntity(it) }

    override suspend fun saveFriends(friends: List<Person>) {
        localRepository.clearFriends()
        localRepository.insertFriends(friends.map { it.toEntity() })
    }
}