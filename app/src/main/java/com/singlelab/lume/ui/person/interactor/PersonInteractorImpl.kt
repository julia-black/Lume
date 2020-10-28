package com.singlelab.lume.ui.person.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.profile.Profile
import com.singlelab.net.model.person.ReportPersonRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.person.PersonRepository

class PersonInteractorImpl(private val repository: PersonRepository) : PersonInteractor,
    BaseInteractor(repository as BaseRepository) {
    override suspend fun loadPerson(personUid: String): Profile? {
        return Profile.fromResponse(repository.getProfile(personUid))
    }

    override suspend fun addToFriends(personUid: String) {
        return repository.addToFriends(personUid)
    }

    override suspend fun removeFromFriends(personUid: String) {
        return repository.removeFromFriends(personUid)
    }

    override suspend fun sendReport(personUid: String, reasonReport: String) {
        return repository.sendReport(ReportPersonRequest(personUid, reasonReport))
    }
}