package com.singlelab.data.repositories.my_profile

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.profile.Content
import com.singlelab.data.model.profile.Profile
import com.singlelab.data.model.profile.ResponseImageUid
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class MyProfileRepositoryImpl(private val apiUnit: ApiUnit) : MyProfileRepository,
    BaseRepository() {

    @Throws(ApiException::class)
    override suspend fun getProfile(): Profile? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.myProfileApi.getProfileAsync().await() },
            errorMessage = "Не удалось получить профиль"
        )
    }

    @Throws(ApiException::class)
    override suspend fun updateImageProfile(imageStr: String): ResponseImageUid? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.myProfileApi.updateImageProfileAsync(Content(imageStr)).await() },
            errorMessage = "Не удалось получить профиль"
        )
    }
}