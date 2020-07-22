package com.singlelab.lume.ui.person.interactor

import com.singlelab.data.model.ResponseMessage
import com.singlelab.data.model.profile.Profile
import com.singlelab.data.repositories.BaseRepository
import com.singlelab.data.repositories.person.PersonRepository
import com.singlelab.lume.base.BaseInteractor

class PersonInteractorImpl(private val repository: PersonRepository) : PersonInteractor,
    BaseInteractor(repository as BaseRepository) {
    override suspend fun loadPerson(personUid: String): Profile? {
        return repository.getProfile(personUid)
    }

    override suspend fun addToFriends(personUid: String): ResponseMessage? {
        return repository.addToFriends(personUid)
    }

    override suspend fun removeFromFriends(personUid: String): ResponseMessage? {
        return repository.removeFromFriends(personUid)
    }
}