package com.singlelab.lume.ui.friends.interactor

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.model.profile.Person
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.event.ParticipantStatus
import com.singlelab.net.model.person.SearchPersonRequest
import com.singlelab.net.repositories.BaseRepository
import com.singlelab.net.repositories.friends.FriendsRepository

class FriendsInteractorImpl(private val repository: FriendsRepository) : FriendsInteractor,
    BaseInteractor(repository as BaseRepository) {
    override suspend fun getFriends(personUid: String): List<Person>? {
        return repository.getFriends(personUid)?.mapNotNull {
            Person.fromResponse(it)
        }
    }

    override suspend fun search(request: SearchPersonRequest): List<Person>? {
        return repository.search(request)?.mapNotNull {
            Person.fromResponse(it)
        }
    }

    override suspend fun addToFriends(personUid: String) {
        return repository.addToFriends(personUid)
    }

    override suspend fun invitePerson(personUid: String, eventUid: String) {
        val request =
            ParticipantRequest(
                personUid,
                eventUid,
                ParticipantStatus.WAITING_FOR_APPROVE_FROM_USER.id
            )
        return repository.invitePerson(request)
    }

    override suspend fun removeFriend(personUid: String) {
        return repository.removeFriend(personUid)
    }

    override suspend fun confirmFriend(personUid: String) {
        return repository.confirmFriend(personUid)
    }
}