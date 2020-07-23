package com.singlelab.net.repositories.friends

import com.singlelab.net.ApiUnit
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.repositories.BaseRepository

class FriendsRepositoryImpl(private val apiUnit: ApiUnit) : FriendsRepository, BaseRepository() {
    override suspend fun getFriends(): List<PersonResponse>? {
        return listOf(
            PersonResponse("7CA664FB-6B89-48DD-A590-3C458319E156", "Иван Иванов", null),
            PersonResponse("7CA664FB-6B89-48DD-A590-3C458319E156", "Петр Петров", null)
        )
    }

    override suspend fun search(
        searchStr: String,
        pageNumber: Int,
        pageSize: Int
    ): List<PersonResponse>? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.searchPersonAsync(searchStr, pageNumber, pageSize).await() },
            errorMessage = "Не удалось выполнить поиск"
        )
    }

    @Throws(ApiException::class)
    override suspend fun addToFriends(personUid: String) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.addToFriendsAsync(personUid).await() },
            errorMessage = "Не удалось добавить в друзья"
        )
    }
}