package com.singlelab.net.repositories.friends

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.repositories.BaseRepository

class FriendsRepositoryImpl(private val apiUnit: ApiUnit) : FriendsRepository, BaseRepository() {
    override suspend fun getFriends(): List<PersonResponse>? {
        return listOf(
            PersonResponse("7CA664FB-6B89-48DD-A590-3C458319E156", "Иван Иванов", null),
            PersonResponse("7CA664FB-6B89-48DD-A590-3C458319E156", "Петр Петров", null)
        )
    }
}