package com.singlelab.net.repositories.friends

import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.SearchPersonRequest

interface FriendsRepository {
    suspend fun getFriends(personUid: String): List<PersonResponse>?

    suspend fun search(request: SearchPersonRequest): List<PersonResponse>?

    suspend fun addToFriends(personUid: String)

    suspend fun invitePerson(request: ParticipantRequest)

    suspend fun removeFriend(personUid: String)

    suspend fun confirmFriend(personUid: String)

    suspend fun getPersonsFromContacts(phones: List<String>) : List<PersonResponse>?
}