package com.singlelab.data.repositories.person

import com.singlelab.data.model.ResponseMessage
import com.singlelab.data.model.profile.Content
import com.singlelab.data.model.profile.Profile
import com.singlelab.data.model.profile.ResponseImageUid
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class PersonRepositoryImpl(private val apiUnit: ApiUnit) : PersonRepository,
    BaseRepository() {

    override suspend fun getProfile(): Profile? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.getProfileAsync().await() },
            errorMessage = "Не удалось получить профиль"
        )
    }

    override suspend fun getProfile(personUid: String): Profile? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.getProfileAsync(personUid).await() },
            errorMessage = "Не удалось получить профиль"
        )
    }

    override suspend fun updateImageProfile(imageStr: String): ResponseImageUid? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.updateImageProfileAsync(Content(imageStr)).await() },
            errorMessage = "Не удалось получить профиль"
        )
    }

    override suspend fun addToFriends(personUid: String): ResponseMessage? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.addToFriendsAsync(personUid).await() },
            errorMessage = "Не удалось добавить в друзья"
        )
    }

    override suspend fun removeFromFriends(personUid: String): ResponseMessage? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.personApi.removeFromFriendsAsync(personUid).await() },
            errorMessage = "Не удалось удалить из друзей"
        )
    }
}