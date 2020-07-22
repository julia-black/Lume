package com.singlelab.data.repositories.friends

import com.singlelab.data.model.profile.Person
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class FriendsRepositoryImpl(private val apiUnit: ApiUnit) : FriendsRepository, BaseRepository() {
    override suspend fun getFriends(): List<Person>? {
        return listOf(
            Person("7CA664FB-6B89-48DD-A590-3C458319E156", "Иван Иванов", null),
            Person("7CA664FB-6B89-48DD-A590-3C458319E156", "Петр Петров", null)
        )
    }
}