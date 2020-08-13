package com.singlelab.lume.ui.friends.interactor

import com.singlelab.lume.model.profile.Person
import com.singlelab.net.model.person.SearchPersonRequest

interface FriendsInteractor {
    suspend fun getFriends(personUid: String): List<Person>?

    suspend fun search(request: SearchPersonRequest): List<Person>?

    suspend fun addToFriends(personUid: String)

    suspend fun invitePerson(personUid: String, eventUid: String)

    suspend fun removeFriend(personUid: String)

    suspend fun confirmFriend(personUid: String)
}