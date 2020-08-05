package com.singlelab.net.repositories.friends

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.SearchPersonRequest
import com.singlelab.net.repositories.BaseRepository

class FriendsRepositoryImpl(private val apiUnit: ApiUnit) : FriendsRepository, BaseRepository() {
    override suspend fun getFriends(personUid: String): List<PersonResponse>? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.getFriendsAsync(personUid).await() },
            errorMessage = "Не удалось получить друзей"
        )
    }

    override suspend fun search(request: SearchPersonRequest): List<PersonResponse>? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.searchPersonAsync(request).await() },
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
            call = { apiUnit.eventsApi.addParticipantsAsync(request).await() },
            errorMessage = "Не удалось пригласить пользователя"
        )
    }
}