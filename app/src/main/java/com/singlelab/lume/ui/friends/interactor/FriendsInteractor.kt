package com.singlelab.lume.ui.friends.interactor

import com.singlelab.lume.model.profile.Person

interface FriendsInteractor {
    suspend fun getFriends(personUid: String): List<Person>?

    suspend fun search(
        searchStr: String,
        pageNumber: Int,
        pageSize: Int
    ): List<Person>?

    suspend fun addToFriends(personUid: String)

    suspend fun invitePerson(personUid: String, eventUid: String)
}