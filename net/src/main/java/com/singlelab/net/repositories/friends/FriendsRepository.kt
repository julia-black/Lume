package com.singlelab.net.repositories.friends

import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.person.PersonResponse

interface FriendsRepository {
    suspend fun getFriends(): List<PersonResponse>?

    suspend fun search(
        searchStr: String,
        pageNumber: Int,
        pageSize: Int
    ): List<PersonResponse>?

    @Throws(ApiException::class)
    suspend fun addToFriends(personUid: String)
}