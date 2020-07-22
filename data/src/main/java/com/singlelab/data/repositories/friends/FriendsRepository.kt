package com.singlelab.data.repositories.friends

import com.singlelab.data.model.profile.Person

interface FriendsRepository {
    suspend fun getFriends(): List<Person>?
}