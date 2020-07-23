package com.singlelab.lume.ui.friends.interactor

import com.singlelab.lume.model.profile.Person
import com.singlelab.net.exceptions.ApiException

interface FriendsInteractor {
    suspend fun getFriends(): List<Person>?

    suspend fun search(
        searchStr: String,
        pageNumber: Int,
        pageSize: Int
    ): List<Person>?

    @Throws(ApiException::class)
    suspend fun addToFriends(personUid: String)
}