package com.singlelab.net.repositories.friends

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.repositories.BaseRepository

class FriendsRepositoryImpl(private val apiUnit: ApiUnit) : FriendsRepository, BaseRepository() {
    override suspend fun getFriends(personUid: String): List<PersonResponse>? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.getFriendsAsync(personUid).await() },
            errorMessage = "Не удалось выполнить поиск"
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

    override suspend fun addToFriends(personUid: String) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.addToFriendsAsync(personUid).await() },
            errorMessage = "Не удалось добавить в друзья"
        )
    }

    override suspend fun invitePerson(request: ParticipantRequest) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.eventsApi.invitePersonAsync(request).await() },
            errorMessage = "Не удалось пригласить пользователя"
        )
    }
}