package com.singlelab.data.repositories.my_profile

import com.singlelab.data.model.profile.Profile
import com.singlelab.data.net.ApiUnit
import com.singlelab.data.repositories.BaseRepository

class MyProfileRepositoryImpl(private val apiUnit: ApiUnit) : MyProfileRepository,
    BaseRepository() {
    override suspend fun getProfile() : Profile? {
        return safeApiCall(
            apiUnit = apiUnit,
            call = { apiUnit.profileApi.getProfileAsync().await() },
            errorMessage = "Не удалось получить профиль"
        )
    }
}