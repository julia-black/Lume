package com.singlelab.net.repositories.friends

import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.person.PersonResponse

interface FriendsRepository {
    suspend fun getFriends(personUid: String): List<PersonResponse>?

    suspend fun search(
        searchStr: String,
        pageNumber: Int,
        pageSize: Int
    ): List<PersonResponse>?

    suspend fun addToFriends(personUid: String)

    suspend fun invitePerson(request: ParticipantRequest)
}