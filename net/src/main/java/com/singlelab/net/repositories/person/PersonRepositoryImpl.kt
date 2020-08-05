package com.singlelab.net.repositories.person

import com.singlelab.net.ApiUnit
import com.singlelab.net.model.ImageUidResponse
import com.singlelab.net.model.person.ContentRequest
import com.singlelab.net.model.person.PersonResponse
import com.singlelab.net.model.person.ProfileRequest
import com.singlelab.net.model.person.ProfileResponse
import com.singlelab.net.repositories.BaseRepository

class PersonRepositoryImpl(private val apiUnit: ApiUnit) : PersonRepository,
    BaseRepository() {

    override suspend fun getProfile(): ProfileResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.getProfileAsync().await() },
            errorMessage = "Не удалось получить профиль"
        )
    }

    override suspend fun getProfile(personUid: String): ProfileResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.getProfileAsync(personUid).await() },
            errorMessage = "Не удалось получить профиль"
        )
    }

    override suspend fun getFriends(personUid: String): List<PersonResponse>? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.getFriendsAsync(personUid).await() },
            errorMessage = "Не удалось получить друзей"
        )
    }

    override suspend fun updateImageProfile(imageStr: String): ImageUidResponse? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = {
                apiUnit.personApi.updateImageProfileAsync(
                    ContentRequest(
                        imageStr
                    )
                ).await()
            },
            errorMessage = "Не удалось получить профиль"
        )
    }

    override suspend fun addToFriends(personUid: String) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.addToFriendsAsync(personUid).await() },
            errorMessage = "Не удалось добавить в друзья"
        )
    }

    override suspend fun removeFromFriends(personUid: String) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.removeFromFriendsAsync(personUid).await() },
            errorMessage = "Не удалось удалить из друзей"
        )
    }

    override suspend fun updateProfile(profileRequest: ProfileRequest) {
        safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.updateProfileAsync(profileRequest).await() },
            errorMessage = "Не удалось обновить профиль"
        )
    }
}