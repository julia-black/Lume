package com.singlelab.data.repositories.friends

import com.singlelab.data.model.profile.Person
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class FriendsRepositoryImpl(private val apiUnit: ApiUnit) : FriendsRepository, BaseRepository() {
    override suspend fun getFriends(): List<Person>? {
        return listOf(
            Person("123", "Иван Иванов", null),
            Person("1234", "Петр Петров", null)
        )
    }
}