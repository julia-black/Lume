package com.singlelab.net.repositories.friends

import com.singlelab.net.model.person.PersonResponse

interface FriendsRepository {
    suspend fun getFriends(): List<PersonResponse>?
}